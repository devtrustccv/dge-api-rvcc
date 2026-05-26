package cv.dge.dge_api_rvcc.domain.pedido.business;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;

public interface PedidoRvccBus {

    PedidoRvccResponse criarPedido(PedidoRvccRequest request);
}
