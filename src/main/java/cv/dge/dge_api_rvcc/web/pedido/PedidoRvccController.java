package cv.dge.dge_api_rvcc.web.pedido;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.application.pedido.service.PedidoRvccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Pedidos RVCC")
@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoRvccController {

    private final PedidoRvccService pedidoRvccService;

    @Operation(summary = "Cria um pedido RVCC com documento opcional")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoRvccResponse criar(
            @RequestPart("data") PedidoRvccRequest request,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        return pedidoRvccService.criarPedido(request, file);
    }
}
