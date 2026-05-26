package cv.dge.dge_api_rvcc.application.integration.rvcc.service;

import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;

public interface QualificacaoRvccImportacaoService {

    ImportacaoQualificacoesRvccRequest importar(ImportacaoQualificacoesRvccRequest request);
}
