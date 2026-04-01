package cv.dge.dge_api_rvcc.aplication.acompanhamento.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cv.dge.dge_api_rvcc.aplication.acompanhamento.dto.AcompanhamentoDTO;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AcompanhamentoService {

    private static final Logger log = LogManager.getLogger(AcompanhamentoService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${link.api.acompanhamento}")
    private String link;

    public AcompanhamentoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.restTemplate = new RestTemplate();
    }

    public void criarAcompanhamento(AcompanhamentoDTO body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            String jsonPayload = objectMapper.writeValueAsString(body);
            log.info("JSON que sera enviado para a API: {}", jsonPayload);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
            String url = buildUrl("process");
            log.info("Enviando requisicao para: {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Acompanhamento criado com sucesso! Resposta: {}", response.getBody());
            } else {
                log.warn(
                        "Resposta nao sucedida. Status: {}, Body: {}",
                        response.getStatusCode(),
                        response.getBody()
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar AcompanhamentoDTO para JSON", e);
        } catch (HttpClientErrorException e) {
            log.error("ERRO 400 - Bad Request na API de acompanhamento");
            log.error("Status: {}", e.getStatusCode());
            log.error("Response Body: {}", e.getResponseBodyAsString());
            log.error("Headers: {}", e.getResponseHeaders());
            try {
                log.debug("Body object: {}", objectMapper.writeValueAsString(body));
            } catch (JsonProcessingException ex) {
                log.debug("Nao foi possivel serializar body para debug");
            }
        } catch (Exception e) {
            log.error("Falha na comunicacao com a API de acompanhamento: {}", e.getMessage(), e);
        }
    }

    public void criarAcompanhamentoUpadate(AcompanhamentoDTO body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));

            String jsonPayload = objectMapper.writeValueAsString(body);
            log.info("JSON que sera enviado para a API: {}", jsonPayload);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
            String url = buildUrl("process/update");
            log.info("Enviando requisicao para: {}", url);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Acompanhamento atualizado com sucesso! Resposta: {}", response.getBody());
            } else {
                log.warn(
                        "Resposta nao sucedida. Status: {}, Body: {}",
                        response.getStatusCode(),
                        response.getBody()
                );
            }
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar Atualizar AcompanhamentoDTO para JSON", e);
        } catch (HttpClientErrorException e) {
            log.error("ERRO 400 - Bad Request na API de acompanhamento");
            log.error("Status: {}", e.getStatusCode());
            log.error("Response Body: {}", e.getResponseBodyAsString());
            log.error("Headers: {}", e.getResponseHeaders());
            try {
                log.debug("Body object: {}", objectMapper.writeValueAsString(body));
            } catch (JsonProcessingException ex) {
                log.debug("Nao foi possivel serializar body para debug");
            }
        } catch (Exception e) {
            log.error("Falha na comunicacao com a API de acompanhamento: {}", e.getMessage(), e);
        }
    }

    private String buildUrl(String path) {
        if (link.endsWith("/")) {
            return link + path;
        }
        return link + "/" + path;
    }
}
