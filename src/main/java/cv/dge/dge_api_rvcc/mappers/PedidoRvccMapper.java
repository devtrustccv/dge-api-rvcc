package cv.dge.dge_api_rvcc.mappers;

import cv.dge.dge_api_rvcc.domain.pedido.dtos.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.AgendamentoIo;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Candidato;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.ProcessoRvcc;

public final class PedidoRvccMapper {

    private PedidoRvccMapper() {
    }

    public static PedidoRvccResponse toResponse(
            Candidato candidato,
            ProcessoRvcc processo,
            AgendamentoIo agendamentoIo
    ) {
        return new PedidoRvccResponse(
                candidato.getIdCandidato(),
                processo.getIdProcesso(),
                agendamentoIo.getIdAgendamento(),
                processo.getNumProcesso(),
                processo.getDataSubmissao(),
                "Pedido de rvcc criado com sucesso."
        );
    }
}

