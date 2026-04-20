package cv.dge.dge_api_rvcc.web.controller;

import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.service.QualificacaoRvccImportacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Importacao de Qualificacoes RVCC")
@RestController
@RequestMapping("/v1/qualificacoes-rvcc")
@RequiredArgsConstructor
public class QualificacaoRvccImportacaoController {

    private final QualificacaoRvccImportacaoService qualificacaoRvccImportacaoService;

    @Operation(summary = "Importa qualificacoes RVCC detalhadas e sincroniza UC/AP")
    @PostMapping("/importacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ImportacaoQualificacoesRvccRequest importar(@RequestBody ImportacaoQualificacoesRvccRequest request) {
        return qualificacaoRvccImportacaoService.importar(request);
    }
}
