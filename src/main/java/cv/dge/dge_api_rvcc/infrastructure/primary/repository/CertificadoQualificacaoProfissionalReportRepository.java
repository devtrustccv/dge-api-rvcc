package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.application.report.dto.CandidatoReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.EntidadeFormadoraReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.QualificacaoReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.UnidadeCompetenciaReportDto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CertificadoQualificacaoProfissionalReportRepository {

    Optional<DadosCertificado> obterDadosCertificado(Integer idProcesso, Integer idQualificacao);

    List<UnidadeCompetenciaReportDto> obterUnidadesCompetencia(Integer idProcesso, Integer idQualificacao);

    LocalDate obterDataAvaliacao(Integer idProcesso, Integer idQualificacao);

    record DadosCertificado(
            Integer idProcesso,
            String numProcesso,
            LocalDate dataGeracaoCertificado,
            String dataFinalizacaoFormacao,
            String dataSubmissao,
            CandidatoReportDto candidato,
            EntidadeFormadoraReportDto entidadeFormadora,
            QualificacaoReportDto qualificacao,
            Integer idEntidade
    ) {
    }
}
