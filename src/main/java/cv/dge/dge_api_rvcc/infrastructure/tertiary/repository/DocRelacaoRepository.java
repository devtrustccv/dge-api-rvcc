package cv.dge.dge_api_rvcc.infrastructure.tertiary.repository;

import cv.dge.dge_api_rvcc.infrastructure.tertiary.DocRelacaoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRelacaoRepository extends JpaRepository<DocRelacaoEntity, Integer> {

    List<DocRelacaoEntity> findByIdRelacaoAndTipoRelacaoAndAppCode(
            Long idRelacao, String tipoRelacao, String appCode);
}
