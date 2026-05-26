package cv.dge.dge_api_rvcc.application.report.service;

import cv.dge.dge_api_rvcc.application.report.dto.CertificadoQualificacaoProfissionalReportResponse;
import java.util.Optional;

public interface CertificadoQualificacaoProfissionalReportService {

    Optional<CertificadoQualificacaoProfissionalReportResponse> obterDados(Integer idProcesso, Integer idQualificacao);
}
