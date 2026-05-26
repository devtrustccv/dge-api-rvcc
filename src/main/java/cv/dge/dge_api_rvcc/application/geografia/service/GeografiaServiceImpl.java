package cv.dge.dge_api_rvcc.application.geografia.service;

import cv.dge.dge_api_rvcc.application.geografia.dto.ComboboxDto;
import cv.dge.dge_api_rvcc.application.geografia.enums.TypeGeografia;
import cv.dge.dge_api_rvcc.application.geografia.mapper.GeografiaMapper;
import cv.dge.dge_api_rvcc.domain.geografia.business.GeografiaBus;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeografiaServiceImpl implements GeografiaService {

    private final GeografiaBus geografiaBus;

    @Override
    public List<Map<String, String>> findIlhaByPais(String pais) {
        return geografiaBus.findIlhaByPais(pais)
                .stream()
                .map(GeografiaMapper::toLegacyCombobox)
                .toList();
    }

    @Override
    public List<Map<String, String>> findConcelhoByIlha(String ilha) {
        return geografiaBus.findConcelhoByIlha(ilha)
                .stream()
                .map(GeografiaMapper::toLegacyCombobox)
                .toList();
    }

    @Override
    public List<Map<String, String>> findNacionalidade() {
        return geografiaBus.findNacionalidade()
                .stream()
                .map(GeografiaMapper::toLegacyCombobox)
                .toList();
    }

    @Override
    public List<ComboboxDto> getLocalidadeByPai(TypeGeografia tipo, String idPai) {
        return geografiaBus.getLocalidadeByPai(tipo, idPai)
                .stream()
                .map(GeografiaMapper::toCombobox)
                .toList();
    }

    @Override
    public String findNameById(String id) {
        return geografiaBus.findNameById(id);
    }
}
