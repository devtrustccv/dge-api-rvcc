package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.AtividadeUnidadeCompetencia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtividadeUnidadeCompetenciaRepository extends JpaRepository<AtividadeUnidadeCompetencia, Integer> {

    Optional<AtividadeUnidadeCompetencia> findByUnidadeCompetencia_IdUcAndCodigoAtividade(
            Integer idUc,
            String codigoAtividade
    );
}
