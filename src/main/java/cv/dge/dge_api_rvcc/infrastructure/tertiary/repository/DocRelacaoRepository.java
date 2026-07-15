package cv.dge.dge_api_rvcc.infrastructure.tertiary.repository;

import cv.dge.dge_api_rvcc.infrastructure.tertiary.DocRelacaoEntity;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRelacaoRepository extends JpaRepository<DocRelacaoEntity, Integer> {

    List<DocRelacaoEntity> findByIdRelacaoAndTipoRelacaoAndAppCode(
            BigDecimal idRelacao, String tipoRelacao, String appCode);

    List<DocRelacaoEntity> findByIdRelacaoAndTipoRelacao(
            BigDecimal idRelacao, String tipoRelacao);
}
