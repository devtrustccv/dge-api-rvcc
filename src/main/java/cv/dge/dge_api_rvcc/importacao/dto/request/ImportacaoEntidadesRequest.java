package cv.dge.dge_api_rvcc.importacao.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImportacaoEntidadesRequest(List<EntidadeImportacaoItemRequest> data) {
}
