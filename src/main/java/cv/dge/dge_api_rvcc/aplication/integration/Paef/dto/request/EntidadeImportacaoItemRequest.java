package cv.dge.dge_api_rvcc.aplication.integration.Paef.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EntidadeImportacaoItemRequest(
        String nif,
        @JsonProperty("designacao_comercial")
        String designacaoComercial,
        String ilha,
        String concelho,
        @JsonAlias({"id_concelho", "id_conselho"})
        Integer idConcelho,
        String endereco,
        @JsonProperty("num_alvara")
        String numAlvara,
        @JsonProperty("estado_alvara")
        String estadoAlvara,
        @JsonProperty("qualificacoes_ativas")
        List<QualificacaoImportacaoRequest> qualificacoesAtivas,
        @JsonProperty("qualificacoes_inativas")
        List<QualificacaoImportacaoRequest> qualificacoesInativas
) {
}

