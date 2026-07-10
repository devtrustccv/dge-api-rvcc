package cv.dge.dge_api_rvcc.infrastructure.emprego;

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
@Table(name = "emprego_t_agendamento_entrevista")
public class AgendamentoEntrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_acolhimento")
    private Integer idAcolhimento;

    @Column(name = "dm_status_entrevista")
    private String dmStatusEntrevista;

    @Column(name = "parecer_io")
    private String parecerIo;

    @Column(name = "tipo_servico")
    private String tipoServico;
}
