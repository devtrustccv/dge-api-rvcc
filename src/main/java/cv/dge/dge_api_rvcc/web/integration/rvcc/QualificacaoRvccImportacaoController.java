package cv.dge.dge_api_rvcc.web.integration.rvcc;

import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.service.QualificacaoRvccImportacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Importacao de Qualificacoes RVCC")
@RestController
@RequestMapping({"/v1/qualificacoes-rvcc", "/qualificacoes-rvcc"})
@RequiredArgsConstructor
public class QualificacaoRvccImportacaoController {

    private final QualificacaoRvccImportacaoService qualificacaoRvccImportacaoService;

    @Operation(summary = "Importa qualificacoes RVCC detalhadas e sincroniza UC/AP")
    @PostMapping("/importacao")
    public ResponseEntity<ImportacaoQualificacoesRvccRequest> importar(@RequestBody ImportacaoQualificacoesRvccRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(qualificacaoRvccImportacaoService.importar(request));
    }
}
