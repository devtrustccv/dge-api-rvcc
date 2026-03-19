package cv.dge.dge_api_rvcc.persistence.repository;

import cv.dge.dge_api_rvcc.persistence.entity.EntidadeQualificacao;
import cv.dge.dge_api_rvcc.persistence.entity.EntidadeQualificacaoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntidadeQualificacaoRepository extends JpaRepository<EntidadeQualificacao, EntidadeQualificacaoId> {

    List<EntidadeQualificacao> findAllByEntidade_IdEntidade(Integer idEntidade);
}
