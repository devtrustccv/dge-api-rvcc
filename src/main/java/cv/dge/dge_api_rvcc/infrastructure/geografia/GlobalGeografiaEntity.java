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
@Table(name = "global_geografia")
public class GlobalGeografiaEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "ILHA")
    private String ilha;

    @Column(name = "CONCELHO")
    private String concelho;

    @Column(name = "PAIS")
    private String pais;

    @Column(name = "FREGUESIA")
    private String freguesia;

    @Column(name = "ZONA")
    private String zona;

    @Column(name = "SELF_ID")
    private String selfId;
}
