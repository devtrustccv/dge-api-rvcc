package cv.dge.dge_api_rvcc.mappers;

import cv.dge.dge_api_rvcc.dtos.EntidadeSelectOptionDto;
import cv.dge.dge_api_rvcc.persistence.entity.Entidade;

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
