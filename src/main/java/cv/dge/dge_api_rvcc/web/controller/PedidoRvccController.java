package cv.dge.dge_api_rvcc.web.controller;

import cv.dge.dge_api_rvcc.domain.pedido.dtos.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.domain.pedido.dtos.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.services.PedidoRvccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pedidos RVCC")
@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoRvccController {

    private final PedidoRvccService pedidoRvccService;

    @Operation(summary = "Cria um pedido RVCC")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoRvccResponse criar(@RequestBody PedidoRvccRequest request) {
        return pedidoRvccService.criarPedido(request);
    }
}

