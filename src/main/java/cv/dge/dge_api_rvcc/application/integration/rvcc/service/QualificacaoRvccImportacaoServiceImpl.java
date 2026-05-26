package cv.dge.dge_api_rvcc.application.integration.rvcc.service;

import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;
import cv.dge.dge_api_rvcc.domain.integration.rvcc.business.QualificacaoRvccImportacaoBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QualificacaoRvccImportacaoServiceImpl implements QualificacaoRvccImportacaoService {

    private final QualificacaoRvccImportacaoBus importacaoBus;

    @Override
    public ImportacaoQualificacoesRvccRequest importar(ImportacaoQualificacoesRvccRequest request) {
        return importacaoBus.importar(request);
    }
}
