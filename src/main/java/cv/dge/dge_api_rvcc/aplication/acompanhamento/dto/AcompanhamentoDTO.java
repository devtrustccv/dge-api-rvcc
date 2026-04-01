package cv.dge.dge_api_rvcc.aplication.acompanhamento.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcompanhamentoDTO {

    @JsonProperty("numero")
    private String numero;

    @JsonProperty("app_dad")
    private String appDad;

    @JsonProperty("pessoa_id")
    private Integer pessoaId;

    @JsonProperty("entidade_nif")
    private String entidadeNif;

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("percentagem")
    private Integer percentagem;

    @JsonProperty("entidade")
    private String entidade;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("data_fim")
    private LocalDateTime dataFim;

    @JsonProperty("data_inicio")
    private LocalDateTime dataInicio;

    @JsonProperty("data_fim_previsto")
    private LocalDate dataFimPrevisto;

    @JsonProperty("etapa_atual")
    private String etapaAtual;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("estado_desc")
    private String estadoDesc;

    @JsonProperty("detalhes")
    private Map<String, String> detalhes;

    @JsonProperty("comunicacoes")
    private List<Comunicacao> comunicacoes;

    @JsonProperty("eventos")
    private List<Evento> eventos;

    @JsonProperty("outputs")
    private List<Output> outputs;

    @JsonProperty("anexos")
    private List<Anexo> anexos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comunicacao {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("descricao")
        private String descricao;

        @JsonProperty("datetime")
        private LocalDateTime datetime;

        @JsonProperty("items")
        private Map<String, String> items;
        @JsonProperty("urls")
        private List<UrlItem> urls;


    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UrlItem {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("url")
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Evento {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("descricao")
        private String descricao;

        @JsonProperty("data")
        private LocalDateTime data;

        @JsonProperty("items")
        private Map<String, String> items;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Output {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("url")
        private String url;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Anexo {
        @JsonProperty("titulo")
        private String titulo;

        @JsonProperty("datetime")
        private LocalDateTime datetime;

        @JsonProperty("url")
        private String url;

        @JsonProperty("input")
        private boolean input;
    }
}
