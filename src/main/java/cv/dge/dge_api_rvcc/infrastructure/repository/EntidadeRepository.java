package cv.dge.dge_api_rvcc.persistence.repository;

import cv.dge.dge_api_rvcc.persistence.entity.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntidadeRepository extends JpaRepository<Entidade, Integer> {

    Optional<Entidade> findByNif(String nif);
}
