package cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImportacaoQualificacoesRvccRequest(List<QualificacaoRvccItemRequest> data) {
}
