package cv.dge.dge_api_rvcc.infrastructure.emprego.repository;

import cv.dge.dge_api_rvcc.infrastructure.emprego.DetalhesAcolhimento;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalhesAcolhimentoRepository extends JpaRepository<DetalhesAcolhimento, Integer> {

    Optional<DetalhesAcolhimento> findByNumInscricao(String numInscricao);
}
