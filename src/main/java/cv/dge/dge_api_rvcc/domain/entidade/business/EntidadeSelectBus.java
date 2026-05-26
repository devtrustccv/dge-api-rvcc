package cv.dge.dge_api_rvcc.domain.entidade.business;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;
import java.util.List;

public interface EntidadeSelectBus {

    List<Entidade> listarEntidadesIoParaSelect();
}
