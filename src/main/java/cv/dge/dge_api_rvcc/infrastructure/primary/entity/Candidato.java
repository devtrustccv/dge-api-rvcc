package cv.dge.dge_api_rvcc.infrastructure.primary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rvcc_t_candidato")
public class Candidato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_candidato")
    private Integer idCandidato;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "sexo")
    private String sexo;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "validade_documento")
    private LocalDate validadeDocumento;

    @Column(name = "data_emissao_documento")
    private LocalDate dataEmissaoDocumento;

    @Column(name = "nif")
    private String nif;

    @Column(name = "nacionalidade")
    private String nacionalidade;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Column(name = "morada")
    private String morada;

    @Column(name = "endereco_atual")
    private String enderecoAtual;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "situacao_emprego")
    private String situacaoEmprego;

    @Column(name = "empregado")
    private String empregado;

    @Column(name = "entidade_empregadora")
    private String entidadeEmpregadora;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "habilitacoes_literarias")
    private String habilitacoesLiterarias;

    @Column(name = "disponibilidade")
    private String disponibilidade;

    @Column(name = "idade")
    private Integer idade;

    @Column(name = "ilha")
    private String ilha;

    @Column(name = "concelho")
    private String concelho;

    @Column(name = "estado")
    private String estado;

    @Column(name = "utilizador_registo")
    private String utilizadorRegisto;

    @Column(name = "data_registo")
    private LocalDateTime dataRegisto;

    @Column(name = "id_pessoa")
    private Integer idPessoa;
}

