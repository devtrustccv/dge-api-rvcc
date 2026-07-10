package cv.dge.dge_api_rvcc.web.saida;

import cv.dge.dge_api_rvcc.application.saida.dto.SaidaProfissionalSelectDto;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.SaidaProfissionalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Saídas Profissionais")
@RestController
@RequestMapping("/saidas-profissionais")
@RequiredArgsConstructor
public class SaidaProfissionalController {

    private final SaidaProfissionalRepository saidaProfissionalRepository;

    @Operation(summary = "Lista saídas profissionais para preencher select")
    @GetMapping("/select")
    public List<SaidaProfissionalSelectDto> listarParaSelect() {
        return saidaProfissionalRepository.findAllForSelect();
    }
}
