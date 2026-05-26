package cv.dge.dge_api_rvcc.domain.integration.rvcc.business;

import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;

public interface QualificacaoRvccImportacaoBus {

    ImportacaoQualificacoesRvccRequest importar(ImportacaoQualificacoesRvccRequest request);
}
