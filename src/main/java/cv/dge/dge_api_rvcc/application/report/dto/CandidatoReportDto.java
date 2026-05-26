package cv.dge.dge_api_rvcc.application.report.dto;

public record CandidatoReportDto(
        String nome,
        String naturalidade,
        String concelho,
        String nacionalidade,
        String dataNascimento,
        String tipoDocumento,
        String numeroDocumento,
        String documentoIdentificacao
) {
}
