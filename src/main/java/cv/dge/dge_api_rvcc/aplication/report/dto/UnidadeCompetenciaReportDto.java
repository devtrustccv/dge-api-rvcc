package cv.dge.dge_api_rvcc.aplication.report.dto;

public record UnidadeCompetenciaReportDto(
        Integer idUc,
        String codigo,
        String nome,
        Integer cargaHoraria,
        String duracaoFormacao,
        Boolean validada,
        String resultado
) {
}
