package cv.dge.dge_api_rvcc.infrastructure.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cv.dge.dge_api_rvcc.common.exception.IntegracaoOrganizacaoException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class OrganizacaoApiClient {

    private static final String ORGANIZATION_PATH = "/api/organization";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_DAD = "dad";
    private static final String DAD_CERTIFICACAO_EVCC = "certificacao_evcc";
    private static final List<String> CAMPOS_ID = List.of(
            "id_organica",
            "idOrganica",
            "organization_id",
            "organizationId",
            "id"
    );

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public OrganizacaoApiClient(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${link.api.base}") String linkApiBase
    ) {
        this.restClient = restClientBuilder.baseUrl(linkApiBase).build();
        this.objectMapper = objectMapper;
    }

    public String criarOrganizacao(String nomeEntidade) {
        String nomeNormalizado = validarNomeEntidade(nomeEntidade);

        try {
            ResponseEntity<String> response = enviarCriacao(nomeNormalizado);
            return extrairIdObrigatorio(response);
        } catch (RestClientResponseException exception) {
            throw new IntegracaoOrganizacaoException(
                    "Falha ao criar organizacao na API externa. Status HTTP: "
                            + exception.getStatusCode().value() + ".",
                    exception
            );
        } catch (RestClientException exception) {
            throw new IntegracaoOrganizacaoException(
                    "Nao foi possivel comunicar com a API externa de organizacao.",
                    exception
            );
        }
    }

    private String validarNomeEntidade(String nomeEntidade) {
        if (!StringUtils.hasText(nomeEntidade)) {
            throw new IntegracaoOrganizacaoException(
                    "Nao foi possivel criar a organizacao externa porque a entidade nao possui designacao."
            );
        }

        return nomeEntidade.trim();
    }

    private ResponseEntity<String> enviarCriacao(String nomeEntidade) {
        return restClient.post()
                .uri(ORGANIZATION_PATH)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(criarFormulario(nomeEntidade))
                .retrieve()
                .toEntity(String.class);
    }

    private MultiValueMap<String, Object> criarFormulario(String nomeEntidade) {
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add(FIELD_NAME, nomeEntidade);
        formData.add(FIELD_DAD, DAD_CERTIFICACAO_EVCC);
        return formData;
    }

    private String extrairIdObrigatorio(ResponseEntity<String> response) {
        String idDoBody = extrairIdDoBody(response.getBody());
        if (StringUtils.hasText(idDoBody)) {
            return idDoBody;
        }

        String idDaLocation = extrairIdDaLocation(response.getHeaders());
        if (StringUtils.hasText(idDaLocation)) {
            return idDaLocation;
        }

        throw new IntegracaoOrganizacaoException(
                "A API externa nao devolveu o id da organizacao criada."
        );
    }

    private String extrairIdDoBody(String responseBody) {
        if (!StringUtils.hasText(responseBody)) {
            return null;
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            if (root.isValueNode()) {
                return extrairTexto(root);
            }

            for (String campoId : CAMPOS_ID) {
                String id = extrairTexto(root.findValue(campoId));
                if (StringUtils.hasText(id)) {
                    return id;
                }
            }

            return null;
        } catch (IOException exception) {
            return null;
        }
    }

    private String extrairTexto(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }

        String value = node.asText();
        return StringUtils.hasText(value) ? value.trim() : null;
    }

    private String extrairIdDaLocation(HttpHeaders headers) {
        URI location = headers.getLocation();
        if (location == null || !StringUtils.hasText(location.getPath())) {
            return null;
        }

        String[] segmentos = location.getPath().split("/");
        if (segmentos.length == 0) {
            return null;
        }

        for (int indice = segmentos.length - 1; indice >= 0; indice--) {
            String segmento = segmentos[indice];
            if (StringUtils.hasText(segmento)) {
                return segmento.trim();
            }
        }

        return null;
    }
}
