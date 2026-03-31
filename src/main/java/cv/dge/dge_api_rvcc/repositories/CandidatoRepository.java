package cv.dge.dge_api_rvcc.repositories;

import cv.dge.dge_api_rvcc.models.Candidato;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRepository extends JpaRepository<Candidato, Integer> {

    Optional<Candidato> findByNumeroDocumento(String numeroDocumento);
}
