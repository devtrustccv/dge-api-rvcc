package cv.dge.dge_api_rvcc.Utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RestClientHelper {
    private static final Logger logger = LogManager.getLogger(RestClientHelper.class);

    private final RestTemplate restTemplate;

    public RestClientHelper() {
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> sendRequest(
            String url,
            HttpMethod method,
            Object requestBody,
            Class<T> responseType,
            Map<String, String> headersMap) {

        try {
            // Configurar cabeçalhos
            HttpHeaders headers = new HttpHeaders();

            if (headersMap != null) {
                headersMap.forEach((key, value) -> {
                    if (key.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                        // Definir o Content-Type de forma apropriada
                        headers.setContentType(MediaType.parseMediaType(value));
                    } else {
                        headers.set(key, value);
                    }
                });
            } else {
                headers.setContentType(MediaType.APPLICATION_JSON); // padrão
            }

            // Criar entidade de requisição
            HttpEntity<Object> entity = new HttpEntity<>(requestBody, headers);

            // Fazer a requisição e retornar a resposta
            return restTemplate.exchange(url, method, entity, responseType);

        } catch (HttpClientErrorException e) {
            logger.error("Erro HTTP ao acessar '{}': status={} - body={}", url, e.getStatusCode(), e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar requisição para '{}': {}", url, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
