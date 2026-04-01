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
@Table(name = "rvcc_t_entidade")
public class Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entidade")
    private Integer idEntidade;

    @Column(name = "designacao_comercial")
    private String designacaoComercial;

    @Column(name = "ilha")
    private String ilha;

    @Column(name = "concelho")
    private String concelho;

    @Column(name = "id_concelho")
    private Integer idConcelho;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "num_alvara")
    private String numAlvara;

    @Column(name = "estado_alvara")
    private String estadoAlvara;

    @Column(name = "ativo")
    private Boolean ativo = Boolean.TRUE;

    @Column(name = "nif")
    private String nif;

    @Column(name = "id_organica")
    private String idOrganica;
}

