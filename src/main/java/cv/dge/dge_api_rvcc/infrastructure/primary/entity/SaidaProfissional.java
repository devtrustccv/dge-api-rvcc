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
@Table(name = "rvcc_t_saida_profissional")
public class SaidaProfissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_saida_profissional")
    private Integer idSaidaProfissional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_qualificacao")
    private QualificacaoProfissional qualificacao;

    @Column(name = "id_referencial")
    private Integer idReferencial;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "denominacao", nullable = false)
    private String denominacao;

    @Column(name = "estado")
    private String estado;
}
