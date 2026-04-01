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
@Table(name = "rvcc_t_agendamento_io")
public class AgendamentoIo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Integer idAgendamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_processo")
    private ProcessoRvcc idProcesso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entidade_cefp")
    private Entidade idEntidadeCefp;

    @Column(name = "data_agendada")
    private LocalDateTime dataAgendada;

    @Column(name = "data_realizacao")
    private LocalDateTime dataRealizacao;

    @Column(name = "estado")
    private String estado;

    @Column(name = "modalidade")
    private String modalidade;

    @Column(name = "resultado_io")
    private String resultadoIo;

    @Column(name = "recomendacao")
    private String recomendacao;

    @Column(name = "tecnico_entrevistador")
    private String tecnicoEntrevistador;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "utilizador_registo")
    private String utilizadorRegisto;

    @Column(name = "datareg")
    private LocalDateTime datareg;

    @Column(name = "tipo_agendamento")
    private String tipoAgendamento;
}

