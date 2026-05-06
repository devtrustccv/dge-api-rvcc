package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.UnidadeCompetencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnidadeCompetenciaRepository extends JpaRepository<UnidadeCompetencia, Integer> {

    Optional<UnidadeCompetencia> findByIdQualificacao_IdQualificacaoAndIdReferencialAndCodigoUc(
            Integer idQualificacao,
            Integer idReferencial,
            String codigoUc
    );

    Optional<UnidadeCompetencia> findByIdQualificacao_IdQualificacaoAndIdReferencialAndIdUcIntegracao(
            Integer idQualificacao,
            Integer idReferencial,
            Integer idUcIntegracao
    );

    List<UnidadeCompetencia> findAllByIdQualificacao_IdQualificacao(Integer idQualificacao);
}
