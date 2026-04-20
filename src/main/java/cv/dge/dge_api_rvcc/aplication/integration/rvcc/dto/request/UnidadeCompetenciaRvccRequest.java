package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UnidadeCompetenciaRvccRequest(
        Integer id,
        String codigo,
        String denominacao,
        @JsonProperty("codigoModulo")
        String codigoModulo,
        @JsonProperty("denominacaoModulo")
        String denominacaoModulo,
        @JsonProperty("cargaHorariaModulo")
        String cargaHorariaModulo,
        @JsonProperty("atividadesProfissionais")
        List<AtividadeProfissionalRvccRequest> atividadesProfissionais
) {
}
