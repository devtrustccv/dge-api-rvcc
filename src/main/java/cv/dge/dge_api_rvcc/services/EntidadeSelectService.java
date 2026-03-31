package cv.dge.dge_api_rvcc.services;

import cv.dge.dge_api_rvcc.dtos.EntidadeSelectOptionDto;
import cv.dge.dge_api_rvcc.mappers.EntidadeSelectMapper;
import cv.dge.dge_api_rvcc.repositories.TipoEntidadeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntidadeSelectService {

    private static final String CODIGO_TIPO_IO = "IO";

    private final TipoEntidadeRepository tipoEntidadeRepository;

    public List<EntidadeSelectOptionDto> listarEntidadesIoParaSelect() {
        return tipoEntidadeRepository.findEntidadesByCodigo(CODIGO_TIPO_IO).stream()
                .map(EntidadeSelectMapper::toDto)
                .toList();
    }
}
