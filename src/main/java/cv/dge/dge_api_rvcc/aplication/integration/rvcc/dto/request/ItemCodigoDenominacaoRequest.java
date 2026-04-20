package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemCodigoDenominacaoRequest(
        String codigo,
        String denominacao
) {
}
