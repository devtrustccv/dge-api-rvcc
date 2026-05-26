package cv.dge.dge_api_rvcc.application.entidade.mapper;

import cv.dge.dge_api_rvcc.application.entidade.dto.EntidadeSelectOptionDto;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;

public final class EntidadeSelectMapper {

    private EntidadeSelectMapper() {
    }

    public static EntidadeSelectOptionDto toDto(Entidade entidade) {
        return new EntidadeSelectOptionDto(
                entidade.getIdEntidade(),
                entidade.getDesignacaoComercial()
        );
    }
}
