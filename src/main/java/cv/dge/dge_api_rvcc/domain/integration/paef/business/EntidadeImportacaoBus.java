package cv.dge.dge_api_rvcc.domain.integration.paef.business;

import cv.dge.dge_api_rvcc.application.integration.paef.dto.request.ImportacaoEntidadesRequest;

public interface EntidadeImportacaoBus {

    ImportacaoEntidadesRequest importar(ImportacaoEntidadesRequest request);
}
