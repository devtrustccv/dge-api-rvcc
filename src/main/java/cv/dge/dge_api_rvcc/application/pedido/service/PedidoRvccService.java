package cv.dge.dge_api_rvcc.application.pedido.service;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;

public interface PedidoRvccService {

    PedidoRvccResponse criarPedido(PedidoRvccRequest request);
}
