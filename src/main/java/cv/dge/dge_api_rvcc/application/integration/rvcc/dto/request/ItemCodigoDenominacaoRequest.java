package cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemCodigoDenominacaoRequest(
        String codigo,
        String denominacao
) {
}
