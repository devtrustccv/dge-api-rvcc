package cv.dge.dge_api_rvcc.application.pedido.service;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.domain.pedido.business.PedidoRvccBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoRvccServiceImpl implements PedidoRvccService {

    private final PedidoRvccBus pedidoRvccBus;

    @Override
    public PedidoRvccResponse criarPedido(PedidoRvccRequest request) {
        return pedidoRvccBus.criarPedido(request);
    }
}
