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
@Table(name = "rvcc_t_atividade_unidade_competencia")
public class AtividadeUnidadeCompetencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atividade")
    private Integer idAtividade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_uc")
    private UnidadeCompetencia unidadeCompetencia;

    @Column(name = "codigo_atividade")
    private String codigoAtividade;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "ponderacao")
    private Integer ponderacao;

    @Column(name = "requisitos")
    private String requisitos;

    @Column(name = "conhecimentos")
    private String conhecimentos;

    @Column(name = "ativo")
    private Boolean ativo = Boolean.TRUE;
}
