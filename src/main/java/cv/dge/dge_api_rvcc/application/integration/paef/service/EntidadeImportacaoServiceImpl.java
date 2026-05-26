package cv.dge.dge_api_rvcc.application.integration.paef.service;

import cv.dge.dge_api_rvcc.application.integration.paef.dto.request.ImportacaoEntidadesRequest;
import cv.dge.dge_api_rvcc.domain.integration.paef.business.EntidadeImportacaoBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntidadeImportacaoServiceImpl implements EntidadeImportacaoService {

    private final EntidadeImportacaoBus importacaoBus;

    @Override
    public ImportacaoEntidadesRequest importar(ImportacaoEntidadesRequest request) {
        return importacaoBus.importar(request);
    }
}
