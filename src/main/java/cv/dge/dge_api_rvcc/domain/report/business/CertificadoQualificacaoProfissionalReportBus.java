package cv.dge.dge_api_rvcc.domain.report.business;

import cv.dge.dge_api_rvcc.application.report.dto.CertificadoQualificacaoProfissionalReportResponse;
import java.util.Optional;

public interface CertificadoQualificacaoProfissionalReportBus {

    Optional<CertificadoQualificacaoProfissionalReportResponse> obterDados(Integer idProcesso, Integer idQualificacao);
}
