package cv.dge.dge_api_rvcc.controllers;

import cv.dge.dge_api_rvcc.dtos.EntidadeSelectOptionDto;
import cv.dge.dge_api_rvcc.services.EntidadeSelectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Entidades")
@RestController
@RequestMapping("/entidades")
@RequiredArgsConstructor
public class EntidadeSelectController {

    private final EntidadeSelectService entidadeSelectService;

    @Operation(summary = "Lista entidades do tipo IO para preencher select")
    @GetMapping("/io/select")
    public List<EntidadeSelectOptionDto> listarEntidadesIoParaSelect() {
        return entidadeSelectService.listarEntidadesIoParaSelect();
    }
}
