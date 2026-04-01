package cv.dge.dge_api_rvcc.infrastructure.primary.entity;

import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rvcc_t_processo_rvcc")
public class ProcessoRvcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_processo")
    private Integer idProcesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_candidato")
    private Candidato idCandidato;

    @Column(name = "num_processo")
    private String numProcesso;

    @Column(name = "estado")
    private String estado;

    @Column(name = "data_submissao")
    private LocalDateTime dataSubmissao;

    @Column(name = "data_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "utilizador_registo")
    private String utilizadorRegisto;

    @Column(name = "datareg")
    private LocalDateTime datareg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade")
    private Entidade idEntidade;
}

