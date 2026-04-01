package cv.dge.dge_api_rvcc.aplication.notification.service;

import cv.dge.dge_api_rvcc.Utils.RestClientHelper;
import cv.dge.dge_api_rvcc.aplication.notification.dto.DefaultReponseDTO;
import cv.dge.dge_api_rvcc.aplication.notification.dto.NotificationRequestDTO;
import cv.dge.dge_api_rvcc.infrastructure.secondary.TNotificacaoConfigEmail;
import cv.dge.dge_api_rvcc.infrastructure.secondary.repository.NotificacaoConfigEmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final RestClientHelper restClientHelper;

    private final NotificacaoConfigEmailRepository repo;

    @Value("${link.api.base}")
    private String notificationBaseUrl;

    public NotificationService(RestClientHelper restClientHelper, NotificacaoConfigEmailRepository repo) {
        this.restClientHelper = restClientHelper;
        this.repo = repo;
    }

    public DefaultReponseDTO enviarEmail(NotificationRequestDTO dto) {
        String url = notificationBaseUrl + "/api/notification";

        // Construir corpo multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("appName", dto.getAppName());
        body.add("Subject", dto.getAssunto());
        body.add("message", dto.getMensagem());
        body.add("email", dto.getEmail());
        body.add("tipoProcesso", dto.getTipoProcesso());
        body.add("idProcesso", dto.getIdProcesso());
        body.add("tipoRelacao", dto.getTipoRelacao());
        body.add("idRelacao", dto.getIdRelacao());
        body.add("isAlert", dto.getIsAlert() != null ? dto.getIsAlert() : "NAO");




        Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);

        // Enviar a requisição
        ResponseEntity<DefaultReponseDTO> response = restClientHelper.sendRequest(
                url,
                HttpMethod.POST,
                body,
                DefaultReponseDTO.class,
                headers
        );

        return response.getBody();
    }

    public TNotificacaoConfigEmail loadConfigNotification(String codigo, String processo, String etapa, String appCode) {

        if (processo != null && etapa != null) {
            return repo.findFirstByCodigoAndAppCodeAndProcessoCodeAndEtapaCode(
                    codigo, appCode, processo, etapa
            ).orElse(null);
        }

        return repo.findFirstByCodigoAndAppCode(
                codigo, appCode
        ).orElse(null);
    }

}
