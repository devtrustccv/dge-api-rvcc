package cv.dge.dge_api_rvcc.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PedidoRvccRequest(
        @JsonProperty("id_entidade")
        Integer idEntidade,
        @JsonProperty("tipo_documento")
        String tipoDocumento,
        @JsonProperty("numero_documento")
        String numeroDocumento,
        @JsonProperty("data_emissao")
        @JsonAlias("data_emissao_documento")
        LocalDate dataEmissao,
        @JsonProperty("data_validade")
        @JsonAlias("validade_documento")
        LocalDate dataValidade,
        @JsonProperty("nome_completo")
        String nomeCompleto,
        @JsonProperty("data_nascimento")
        LocalDate dataNascimento,
        String nif,
        @JsonAlias("sexo")
        String genero,
        String nacionalidade,
        String ilha,
        String concelho,
        String morada,
        String email,
        @JsonAlias("telefone")
        String telemovel,
        @JsonProperty("situacao_emprego")
        String situacaoEmprego,
        @JsonProperty("entidade_empregadora")
        String entidadeEmpregadora,
        String profissao,
        @JsonProperty("habilitacao_literaria")
        @JsonAlias("habilitacoes_literarias")
        String habilitacaoLiteraria,
        @JsonProperty("disponibilidade_horario")
        @JsonAlias("disponibilidade")
        String disponibilidadeHorario,
        @JsonProperty("idade")
        Integer idade,
        @JsonProperty("id_pessoa")
        Integer idPessoa
) {
}
