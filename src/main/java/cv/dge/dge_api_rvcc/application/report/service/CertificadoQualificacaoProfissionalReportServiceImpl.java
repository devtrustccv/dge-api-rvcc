package cv.dge.dge_api_rvcc.application.report.service;

import cv.dge.dge_api_rvcc.application.report.dto.CertificadoQualificacaoProfissionalReportResponse;
import cv.dge.dge_api_rvcc.domain.report.business.CertificadoQualificacaoProfissionalReportBus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificadoQualificacaoProfissionalReportServiceImpl implements CertificadoQualificacaoProfissionalReportService {

    private final CertificadoQualificacaoProfissionalReportBus reportBus;

    @Override
    public Optional<CertificadoQualificacaoProfissionalReportResponse> obterDados(Integer idProcesso, Integer idQualificacao) {
        return reportBus.obterDados(idProcesso, idQualificacao);
    }
}
