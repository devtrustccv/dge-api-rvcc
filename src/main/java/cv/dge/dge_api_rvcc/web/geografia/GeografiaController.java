package cv.dge.dge_api_rvcc.web.geografia;

import cv.dge.dge_api_rvcc.application.geografia.dto.ComboboxDto;
import cv.dge.dge_api_rvcc.application.geografia.enums.TypeGeografia;
import cv.dge.dge_api_rvcc.application.geografia.service.GeografiaService;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geografia")
@RequiredArgsConstructor
public class GeografiaController {

    private final GeografiaService geografiaService;

    @GetMapping("/ilhas")
    public List<Map<String, String>> getIlhasPorPais(@RequestParam String pais) {
        return geografiaService.findIlhaByPais(pais);
    }

    @GetMapping("/concelho")
    public List<Map<String, String>> getConcelhosPorIlha(@RequestParam String ilha) {
        return geografiaService.findConcelhoByIlha(ilha);
    }

    @GetMapping("/nacionalidade")
    public List<Map<String, String>> getNacionalidade() {
        return geografiaService.findNacionalidade();
    }

    @GetMapping("/localidade")
    public List<ComboboxDto> getLocalidade(@RequestParam TypeGeografia tipo, @RequestParam(required = false) String idPai) {
        return geografiaService.getLocalidadeByPai(tipo, idPai);
    }
}
