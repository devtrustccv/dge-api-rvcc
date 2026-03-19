package cv.dge.dge_api_rvcc.importacao.controller;

import cv.dge.dge_api_rvcc.importacao.dto.request.ImportacaoEntidadesRequest;
import cv.dge.dge_api_rvcc.importacao.dto.response.ImportacaoEntidadesResponse;
import cv.dge.dge_api_rvcc.importacao.service.EntidadeImportacaoService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Importacao de Entidades")
@RestController
@RequestMapping("/v1/entidades")
@RequiredArgsConstructor
public class EntidadeImportacaoController {

    private final EntidadeImportacaoService entidadeImportacaoService;

    @Operation(summary = "Importa entidades de um json")
    @PostMapping("/importacao")
    @ResponseStatus(HttpStatus.CREATED)
    public ImportacaoEntidadesResponse importar(@RequestBody ImportacaoEntidadesRequest request) {
        return entidadeImportacaoService.importar(request);
    }
}

