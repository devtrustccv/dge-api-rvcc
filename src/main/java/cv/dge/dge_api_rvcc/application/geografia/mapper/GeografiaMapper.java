package cv.dge.dge_api_rvcc.application.geografia.mapper;

import cv.dge.dge_api_rvcc.application.geografia.dto.ComboboxDto;
import cv.dge.dge_api_rvcc.infrastructure.geografia.GlobalGeografiaEntity;
import cv.dge.dge_api_rvcc.infrastructure.geografia.VGeograficaEntity;
import java.util.Map;

public final class GeografiaMapper {

    private GeografiaMapper() {
    }

    public static Map<String, String> toLegacyCombobox(GlobalGeografiaEntity geografia) {
        return Map.of(
                "VALOR", geografia.getId(),
                "DESCRICAO", geografia.getNome()
        );
    }

    public static ComboboxDto toCombobox(VGeograficaEntity geografia) {
        return new ComboboxDto(geografia.getLocalidade(), geografia.getIdLocalidade());
    }
}
