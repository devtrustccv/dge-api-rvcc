package cv.dge.dge_api_rvcc.infrastructure.primary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rvcc_t_unidade_competencia")
public class UnidadeCompetencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_uc")
    private Integer idUc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_qualificacao")
    private QualificacaoProfissional idQualificacao;

    @Column(name = "codigo_uc")
    private String codigoUc;

    @Column(name = "denominacao")
    private String denominacao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "carga_horaria")
    private Integer cargaHoraria;

    @Column(name = "ativo")
    private Boolean ativo = Boolean.TRUE;
}
