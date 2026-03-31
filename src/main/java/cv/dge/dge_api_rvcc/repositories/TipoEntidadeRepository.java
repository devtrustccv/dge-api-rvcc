package cv.dge.dge_api_rvcc.repositories;

import cv.dge.dge_api_rvcc.models.TipoEntidade;
import cv.dge.dge_api_rvcc.persistence.entity.Entidade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoEntidadeRepository extends JpaRepository<TipoEntidade, Integer> {

    @Query("""
            select distinct entidade
            from TipoEntidade te
            join te.entidade entidade
            where upper(te.codigo) = upper(:codigo)
            order by entidade.designacaoComercial
            """)
    List<Entidade> findEntidadesByCodigo(@Param("codigo") String codigo);
}
