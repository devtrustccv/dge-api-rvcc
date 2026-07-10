package cv.dge.dge_api_rvcc.infrastructure.emprego.repository;

import cv.dge.dge_api_rvcc.infrastructure.emprego.AgendamentoEntrevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgendamentoEntrevistaRepository extends JpaRepository<AgendamentoEntrevista, Integer> {

    @Query("SELECT COUNT(a) > 0 FROM AgendamentoEntrevista a " +
            "WHERE a.idAcolhimento = :idAcolhimento " +
            "AND a.dmStatusEntrevista = :status " +
            "AND a.parecerIo = :parecer")
    boolean existsEntrevistaValida(
            @Param("idAcolhimento") Integer idAcolhimento,
            @Param("status") String status,
            @Param("parecer") String parecer);
}
