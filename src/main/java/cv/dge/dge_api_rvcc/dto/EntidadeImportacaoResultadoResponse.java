package cv.dge.dge_api_rvcc.dto;

public record EntidadeImportacaoResultadoResponse(
        Integer idEntidade,
        String nif,
        boolean criada,
        int relacoesCriadas,
        int relacoesAtualizadas,
        int relacoesRemovidas
) {
}
