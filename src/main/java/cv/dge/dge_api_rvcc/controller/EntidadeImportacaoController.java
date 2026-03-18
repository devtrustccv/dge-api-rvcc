package cv.dge.dge_api_rvcc.controller;

import cv.dge.dge_api_rvcc.dto.ImportacaoEntidadesRequest;
import cv.dge.dge_api_rvcc.dto.ImportacaoEntidadesResponse;
import cv.dge.dge_api_rvcc.service.EntidadeImportacaoService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

