package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.response;

public record ImportacaoQualificacoesRvccResponse(
        int qualificacoesRecebidas,
        int qualificacoesProcessadas,
        int qualificacoesIgnoradas,
        int qualificacoesCriadas,
        int unidadesCriadas,
        int unidadesAtualizadas,
        int atividadesCriadas,
        int atividadesAtualizadas
) {
}
