package cv.dge.dge_api_rvcc.application.entidade.service;

import cv.dge.dge_api_rvcc.application.entidade.dto.EntidadeSelectOptionDto;
import java.util.List;

public interface EntidadeSelectService {

    List<EntidadeSelectOptionDto> listarEntidadesIoParaSelect();
}
