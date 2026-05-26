package cv.dge.dge_api_rvcc.application.report.dto;

public record QualificacaoReportDto(
        Integer idQualificacao,
        String codigo,
        String nome,
        Integer nivel,
        String familiaProfissional
) {
}
