package cv.dge.dge_api_rvcc.domain.entidade.business;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.TipoEntidadeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntidadeSelectBusImpl implements EntidadeSelectBus {

    private static final String CODIGO_TIPO_IO = "IO";

    private final TipoEntidadeRepository tipoEntidadeRepository;

    @Override
    public List<Entidade> listarEntidadesIoParaSelect() {
        return tipoEntidadeRepository.findEntidadesByCodigo(CODIGO_TIPO_IO);
    }
}
