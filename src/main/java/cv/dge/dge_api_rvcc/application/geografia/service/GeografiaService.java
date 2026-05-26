package cv.dge.dge_api_rvcc.application.geografia.service;

import cv.dge.dge_api_rvcc.application.geografia.dto.ComboboxDto;
import cv.dge.dge_api_rvcc.application.geografia.enums.TypeGeografia;
import java.util.List;
import java.util.Map;

public interface GeografiaService {

    List<Map<String, String>> findIlhaByPais(String pais);

    List<Map<String, String>> findConcelhoByIlha(String ilha);

    List<Map<String, String>> findNacionalidade();

    List<ComboboxDto> getLocalidadeByPai(TypeGeografia tipo, String idPai);

    String findNameById(String id);
}
