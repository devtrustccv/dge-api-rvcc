package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QualificacaoRvccItemRequest(
        @JsonProperty("idQualificacao")
        Integer idQualificacao,
        @JsonProperty("selfId")
        String selfId,
        @JsonProperty("codigoQualif")
        String codigoQualif,
        @JsonProperty("denominacaoQualif")
        String denominacaoQualif,
        Integer nivel,
        @JsonProperty("estadoQualificacao")
        String estadoQualificacao,
        @JsonProperty("codigoFamilia")
        String codigoFamilia,
        @JsonProperty("denominacaoFamilia")
        String denominacaoFamilia,
        @JsonProperty("unidadesCompetencia")
        List<UnidadeCompetenciaRvccRequest> unidadesCompetencia
) {
}
