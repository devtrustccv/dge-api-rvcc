package cv.dge.dge_api_rvcc.infrastructure.geografia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "view_geografia_global")
public class VGeograficaEntity {

    @Id
    @Column(name = "id_localidade")
    private String idLocalidade;

    @Column(name = "localidade")
    private String localidade;

    @Column(name = "id_ilha")
    private String idIlha;

    @Column(name = "ilha")
    private String ilha;

    @Column(name = "id_concelho")
    private String idConcelho;

    @Column(name = "concelho")
    private String concelho;

    @Column(name = "id_freguesia")
    private String idFreguesia;

    @Column(name = "freguesia")
    private String freguesia;

    @Column(name = "id_pais")
    private String idPais;

    @Column(name = "pais")
    private String pais;

    @Column(name = "id_zona")
    private String idZona;

    @Column(name = "zona")
    private String zona;

    @Column(name = "nacionalidade")
    private String nacionalidade;
}
