package cv.dge.dge_api_rvcc.application.pedido.service;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;
import org.springframework.web.multipart.MultipartFile;

public interface PedidoRvccService {

    PedidoRvccResponse criarPedido(PedidoRvccRequest request, MultipartFile file);
}
