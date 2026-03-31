package cv.dge.dge_api_rvcc.dtos;

import java.time.LocalDateTime;

public record PedidoRvccResponse(
        Integer idCandidato,
        Integer idProcesso,
        Integer idAgendamento,
        String numProcesso,
        LocalDateTime dataSubmissao,
        String mensagem
) {
}
