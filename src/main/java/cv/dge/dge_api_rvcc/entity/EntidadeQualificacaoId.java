package cv.dge.dge_api_rvcc.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class EntidadeQualificacaoId implements Serializable {

    @Column(name = "id_entidade")
    private Integer idEntidade;

    @Column(name = "id_qualificacao")
    private Integer idQualificacao;
}
