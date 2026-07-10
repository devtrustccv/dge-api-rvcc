package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.application.saida.dto.SaidaProfissionalSelectDto;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.SaidaProfissional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaidaProfissionalRepository extends JpaRepository<SaidaProfissional, Integer> {

    Optional<SaidaProfissional> findByQualificacao_IdQualificacaoAndIdReferencialAndDenominacao(
            Integer idQualificacao, Integer idReferencial, String denominacao);

    @Query("SELECT new cv.dge.dge_api_rvcc.application.saida.dto.SaidaProfissionalSelectDto(s.idSaidaProfissional, s.denominacao) FROM SaidaProfissional s ORDER BY s.denominacao")
    List<SaidaProfissionalSelectDto> findAllForSelect();
}
