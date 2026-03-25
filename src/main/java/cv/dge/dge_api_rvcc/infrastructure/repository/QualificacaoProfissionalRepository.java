package cv.dge.dge_api_rvcc.persistence.repository;

import cv.dge.dge_api_rvcc.persistence.entity.QualificacaoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QualificacaoProfissionalRepository extends JpaRepository<QualificacaoProfissional, Integer> {

    Optional<QualificacaoProfissional> findByCodigoCnq(String codigoCnq);
}
