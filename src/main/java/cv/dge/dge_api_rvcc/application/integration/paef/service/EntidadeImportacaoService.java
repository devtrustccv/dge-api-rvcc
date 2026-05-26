package cv.dge.dge_api_rvcc.application.integration.paef.service;

import cv.dge.dge_api_rvcc.application.integration.paef.dto.request.ImportacaoEntidadesRequest;

public interface EntidadeImportacaoService {

    ImportacaoEntidadesRequest importar(ImportacaoEntidadesRequest request);
}
