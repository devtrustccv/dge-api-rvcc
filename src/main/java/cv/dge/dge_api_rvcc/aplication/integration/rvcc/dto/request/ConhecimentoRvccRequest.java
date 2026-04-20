package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ConhecimentoRvccRequest(
        @JsonProperty("codigoUf")
        String codigoUf,
        @JsonProperty("denominacaoUf")
        String denominacaoUf,
        @JsonProperty("codigoMf")
        String codigoMf,
        @JsonProperty("denominacaoMf")
        String denominacaoMf,
        String codigo,
        String denominacao,
        String tipo
) {
}
