package cv.dge.dge_api_rvcc.web.controller;

import cv.dge.dge_api_rvcc.aplication.report.dto.CertificadoQualificacaoProfissionalReportResponse;
import cv.dge.dge_api_rvcc.aplication.report.service.CertificadoQualificacaoProfissionalReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Reporter Certificados")
@RestController
@RequestMapping({"/v1/reporter/certificados", "/reporter/certificados"})
@RequiredArgsConstructor
public class CertificadoQualificacaoProfissionalReportController {

    private final CertificadoQualificacaoProfissionalReportService reportService;

    @Operation(summary = "Devolve dados para o certificado de qualificacao profissional")
    @GetMapping("/qualificacao-profissional/{idProcesso}")
    public ResponseEntity<CertificadoQualificacaoProfissionalReportResponse> obterDadosCertificado(
            @PathVariable Integer idProcesso,
            @RequestParam(required = false) Integer idQualificacao
    ) {
        return reportService.obterDados(idProcesso, idQualificacao)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
