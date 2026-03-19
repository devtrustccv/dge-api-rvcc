package cv.dge.dge_api_rvcc.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "rvcc_t_entidade_qualificacao")
public class EntidadeQualificacao {

    @EmbeddedId
    private EntidadeQualificacaoId id;

    @MapsId("idEntidade")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade", nullable = false)
    private Entidade entidade;

    @MapsId("idQualificacao")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_qualificacao", nullable = false)
    private QualificacaoProfissional qualificacao;

    @Column(name = "data_acreditacao")
    private LocalDate dataAcreditacao;

    @Column(name = "estado_acreditacao")
    private String estadoAcreditacao;
}
