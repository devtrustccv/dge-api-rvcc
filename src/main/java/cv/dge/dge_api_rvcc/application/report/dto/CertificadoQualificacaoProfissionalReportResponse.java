package cv.dge.dge_api_rvcc.application.report.dto;

import java.util.List;
import java.util.Map;

public record CertificadoQualificacaoProfissionalReportResponse(
        Integer idProcesso,
        String numProcesso,
        String numeroCertificado,
        String dataEmissao,
        String dataFinalizacaoFormacao,
        String viaCertificacao,
        String codigoContraprovaCertificado,
        String codigoContraprovaAlvara,
        CandidatoReportDto candidato,
        EntidadeFormadoraReportDto entidadeFormadora,
        QualificacaoReportDto qualificacao,
        List<UnidadeCompetenciaReportDto> unidadesCompetencia,
        List<String> saidasProfissionais,
        Map<String, Object> campos
) {
}
