package cv.dge.dge_api_rvcc.infrastructure.geografia.repository;

import cv.dge.dge_api_rvcc.infrastructure.geografia.GlobalGeografiaEntity;
import cv.dge.dge_api_rvcc.infrastructure.geografia.VGeograficaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaGlobalGeografiaRepository extends JpaRepository<GlobalGeografiaEntity, String> {

    Optional<GlobalGeografiaEntity> findFirstByIdOrCodigo(String id, String codigo);

    @Query("SELECT g FROM GlobalGeografiaEntity g WHERE g.pais = :pais AND g.ilha IS NULL")
    List<GlobalGeografiaEntity> findIlhaByPais(@Param("pais") String pais);

    @Query("SELECT g FROM GlobalGeografiaEntity g WHERE g.ilha = :ilha AND g.concelho IS NULL")
    List<GlobalGeografiaEntity> findConcelhoByIlha(@Param("ilha") String ilha);

    @Query("SELECT g FROM GlobalGeografiaEntity g WHERE g.selfId = '0' ORDER BY g.nome")
    List<GlobalGeografiaEntity> findNacionalidade();

    @Query("""
            SELECT v FROM VGeograficaEntity v
            WHERE ((:idPai IS NULL AND v.idIlha IS NULL AND v.idConcelho IS NULL AND v.idZona IS NULL AND v.idFreguesia IS NULL)
                   OR (:tipo = 'PAIS' AND v.idPais IS NULL)
                   OR (:tipo = 'ILHA' AND v.idPais = :idPai AND v.idIlha IS NULL)
                   OR (:tipo = 'CONCELHO' AND v.idIlha = :idPai AND v.idConcelho IS NULL AND v.idFreguesia IS NULL AND v.idZona IS NULL)
                   OR (:tipo = 'ZONA' AND v.idFreguesia = :idPai)
                   OR (:tipo = 'FREGUESIA' AND v.idConcelho = :idPai AND v.idFreguesia IS NULL))
            ORDER BY v.localidade
            """)
    List<VGeograficaEntity> getLocalidades(@Param("tipo") String tipo, @Param("idPai") String idPai);
}
