package cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AtividadeProfissionalRvccRequest(
        String codigo,
        String descricao,
        Integer ponderacao,
        List<ItemCodigoDenominacaoRequest> requisitos,
        List<ConhecimentoRvccRequest> conhecimentos
) {
}
