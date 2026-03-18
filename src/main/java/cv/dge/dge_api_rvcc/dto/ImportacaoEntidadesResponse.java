package cv.dge.dge_api_rvcc.dto;

import java.util.List;

public record ImportacaoEntidadesResponse(
        int totalRecebido,
        int entidadesCriadas,
        int entidadesAtualizadas,
        int qualificacoesCriadas,
        int relacoesCriadas,
        int relacoesAtualizadas,
        int relacoesRemovidas,
        List<EntidadeImportacaoResultadoResponse> resultados
) {
}
