package cv.dge.dge_api_rvcc.importacao.dto.response;

public record EntidadeImportacaoResultadoResponse(
        Integer idEntidade,
        String nif,
        boolean criada,
        int relacoesCriadas,
        int relacoesAtualizadas,
        int relacoesRemovidas
) {
}
