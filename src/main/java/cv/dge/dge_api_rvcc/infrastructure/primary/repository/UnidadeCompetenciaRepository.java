package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.UnidadeCompetencia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadeCompetenciaRepository extends JpaRepository<UnidadeCompetencia, Integer> {

    Optional<UnidadeCompetencia> findByIdQualificacao_IdQualificacaoAndCodigoUc(
            Integer idQualificacao,
            String codigoUc
    );
}
