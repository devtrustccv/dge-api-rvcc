package cv.dge.dge_api_rvcc.application.entidade.service;

import cv.dge.dge_api_rvcc.application.entidade.dto.EntidadeSelectOptionDto;
import cv.dge.dge_api_rvcc.application.entidade.mapper.EntidadeSelectMapper;
import cv.dge.dge_api_rvcc.domain.entidade.business.EntidadeSelectBus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EntidadeSelectServiceImpl implements EntidadeSelectService {

    private final EntidadeSelectBus entidadeSelectBus;

    @Override
    public List<EntidadeSelectOptionDto> listarEntidadesIoParaSelect() {
        return entidadeSelectBus.listarEntidadesIoParaSelect()
                .stream()
                .map(EntidadeSelectMapper::toDto)
                .toList();
    }
}
