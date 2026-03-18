package cv.dge.dge_api_rvcc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QualificacaoImportacaoRequest(
        @JsonProperty("codigo_cnq")
        String codigoCnq,
        @JsonProperty("selfid_qp")
        String selfidQp,
        String denominacao,
        @JsonProperty("familia")
        String familiaProfissional,
        @JsonProperty("codigo_familia")
        String codigoFamilia,
        @JsonProperty("nivel_qnq")
        Integer nivelQnq
) {
}
