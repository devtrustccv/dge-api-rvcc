package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.SaidaProfissional;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaProfissionalRepository extends JpaRepository<SaidaProfissional, Integer> {

    Optional<SaidaProfissional> findByQualificacao_IdQualificacaoAndIdReferencialAndDenominacao(
            Integer idQualificacao, Integer idReferencial, String denominacao);
}
