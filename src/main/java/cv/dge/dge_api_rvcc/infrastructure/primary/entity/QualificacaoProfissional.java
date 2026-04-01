package cv.dge.dge_api_rvcc.infrastructure.primary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rvcc_t_qualificacao_profissional")
public class QualificacaoProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_qualificacao")
    private Integer idQualificacao;

    @Column(name = "codigo_cnq")
    private String codigoCnq;

    @Column(name = "selfid_qp")
    private String selfidQp;

    @Column(name = "denominacao")
    private String denominacao;

    @Column(name = "familia_profissional")
    private String familiaProfissional;

    @Column(name = "nivel_qnq")
    private Integer nivelQnq;

    @Column(name = "ativo")
    private Boolean ativo = Boolean.TRUE;
}

