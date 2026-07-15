package cv.dge.dge_api_rvcc.infrastructure.tertiary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "doc_t_doc_relacao")
public class DocRelacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date_create")
    private LocalDateTime dateCreate;

    @Column(name = "estado")
    private String estado;

    @Column(name = "name")
    private String name;

    @Column(name = "id_relacao")
    private BigDecimal idRelacao;

    @Column(name = "id_tp_doc")
    private Long idTpDoc;

    @Column(name = "mimetype")
    private String mimetype;

    @Column(name = "path")
    private String path;

    @Column(name = "tipo_relacao")
    private String tipoRelacao;

    @Column(name = "user_create")
    private String userCreate;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "app_code")
    private String appCode;
}
