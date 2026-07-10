package cv.dge.dge_api_rvcc.web.pedido;

import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.application.pedido.dto.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.application.pedido.service.PedidoRvccService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoRvccResponse criar(
            @RequestParam("id_entidade") Integer idEntidade,
            @RequestParam("tipo_documento") String tipoDocumento,
            @RequestParam("numero_documento") String numeroDocumento,
            @RequestParam("data_emissao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataEmissao,
            @RequestParam("data_validade") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataValidade,
            @RequestParam("nome_completo") String nomeCompleto,
            @RequestParam("data_nascimento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataNascimento,
            @RequestParam("nif") String nif,
            @RequestParam("genero") String genero,
            @RequestParam("nacionalidade") String nacionalidade,
            @RequestParam("ilha") String ilha,
            @RequestParam("concelho") String concelho,
            @RequestParam(value = "morada", required = false) String morada,
            @RequestParam(value = "endereco_atual", required = false) String enderecoAtual,
            @RequestParam("email") String email,
            @RequestParam("telemovel") String telemovel,
            @RequestParam(value = "situacao_emprego", required = false) String situacaoEmprego,
            @RequestParam(value = "empregado", required = false) String empregado,
            @RequestParam(value = "entidade_empregadora", required = false) String entidadeEmpregadora,
            @RequestParam(value = "profissao", required = false) String profissao,
            @RequestParam(value = "habilitacao_literaria", required = false) String habilitacaoLiteraria,
            @RequestParam(value = "disponibilidade_horario", required = false) String disponibilidadeHorario,
            @RequestParam(value = "idade", required = false) Integer idade,
            @RequestParam(value = "id_pessoa", required = false) Integer idPessoa,
            @RequestParam("cod_acolhimento") String codAcolhimento,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        PedidoRvccRequest request = new PedidoRvccRequest(
                idEntidade, tipoDocumento, numeroDocumento,
                dataEmissao, dataValidade, nomeCompleto, dataNascimento,
                nif, genero, nacionalidade, ilha, concelho,
                morada, enderecoAtual, email, telemovel,
                situacaoEmprego, empregado, entidadeEmpregadora,
                profissao, habilitacaoLiteraria, disponibilidadeHorario,
                idade, idPessoa, codAcolhimento
        );
        return pedidoRvccService.criarPedido(request, file);
    }
}
