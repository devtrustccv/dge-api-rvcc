package cv.dge.dge_api_rvcc.services;

import cv.dge.dge_api_rvcc.aplication.acompanhamento.dto.AcompanhamentoDTO;
import cv.dge.dge_api_rvcc.aplication.acompanhamento.service.AcompanhamentoService;
import cv.dge.dge_api_rvcc.aplication.notification.dto.NotificationRequestDTO;
import cv.dge.dge_api_rvcc.aplication.notification.service.NotificationService;
import cv.dge.dge_api_rvcc.domain.pedido.dtos.PedidoRvccRequest;
import cv.dge.dge_api_rvcc.domain.pedido.dtos.PedidoRvccResponse;
import cv.dge.dge_api_rvcc.exceptions.PedidoRvccInvalidoException;
import cv.dge.dge_api_rvcc.infrastructure.secondary.TNotificacaoConfigEmail;
import cv.dge.dge_api_rvcc.mappers.PedidoRvccMapper;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.AgendamentoIo;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Candidato;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.ProcessoRvcc;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.EntidadeRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.AgendamentoIoRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.CandidatoRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.ProcessoRvccRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoRvccService {

    private static final String ESTADO_ATIVO = "A";
    private static final String ESTADO_CANDIDATURA = "Candidatura";
    private static final String TIPO_AGENDAMENTO_VALIDACAO = "VALIDACAO";
    private static final String UTILIZADOR_SISTEMA = "";
    private static final String MENSAGEM_IDADE_INVALIDA =
            "Para solicitar o RVCC, a idade tem de ser maior ou igual a 25 anos";
    private static final String MENSAGEM_DATA_EMISSAO_INVALIDA =
            "A \"Data emissao\" nao deve ser superior a data atual. Verifique, por favor.";
    private static final String MENSAGEM_DATA_VALIDADE_INVALIDA =
            "A \"Data validade\" deve ser superior a data atual. Verifique, por favor.";
    private static final String MENSAGEM_INTERVALO_DATA_INVALIDO =
            "A \"Data de emissao\" nao deve ser igual ou superior a \"Data validade\". Verifique, por favor.";
    private static final String MENSAGEM_DATA_NASCIMENTO_INVALIDA =
            "A \"Data de Nascimento\" nao deve ser igual ou maior que a data atual. Verifique, por favor.";

    private final CandidatoRepository candidatoRepository;
    private final ProcessoRvccRepository processoRvccRepository;
    private final AgendamentoIoRepository agendamentoIoRepository;
    private final EntidadeRepository entidadeRepository;
    private final AcompanhamentoService acompanhamentoService;
    private final NotificationService notificationService;

    @Transactional
    public PedidoRvccResponse criarPedido(PedidoRvccRequest request) {
        log.info("Iniciando criacao de pedido RVCC: {}", request);
        try {
            if (request == null) {
                throw new PedidoRvccInvalidoException("O payload do pedido de validacao e obrigatorio.");
            }

            validarObrigatorio(request.numeroDocumento(), "numero_documento");

            String utilizadorRegisto = UTILIZADOR_SISTEMA;
            LocalDateTime agora = LocalDateTime.now();
            String numeroDocumento = normalizar(request.numeroDocumento());

            Optional<Candidato> candidatoExistente = candidatoRepository.findByNumeroDocumento(numeroDocumento);
            Candidato candidato;
            if (candidatoExistente.isPresent()) {
                // Atualizar os dados do candidato existente com os dados enviados da tela
                candidato = candidatoExistente.get();
                DadosCandidato dados = consolidarDados(request);
                validarCamposFinais(dados);
                validarTelemovel(dados.telemovel());
                validarDatas(dados.dataEmissao(), dados.dataValidade(), dados.dataNascimento());
                Integer idadeCalculada = calcularIdade(dados.dataNascimento());
                validarIdade(idadeCalculada);
                preencherCandidato(candidato, dados, idadeCalculada, utilizadorRegisto, agora);
                candidato = candidatoRepository.save(candidato);
            } else {
                candidato = criarCandidato(request, utilizadorRegisto, agora);
            }
            Entidade entidade = obterEntidadeObrigatoria(request.idEntidade());

            ProcessoRvcc processo = new ProcessoRvcc();
            processo.setIdCandidato(candidato);
            processo.setNumProcesso(gerarNumeroProcesso());
            processo.setEstado(ESTADO_CANDIDATURA);
            processo.setDataSubmissao(agora);
            processo.setUtilizadorRegisto(utilizadorRegisto);
            processo.setDatareg(agora);
            processo.setIdEntidade(entidade);
            processo = processoRvccRepository.save(processo);
            log.info("Processo criado: {}", processo.getNumProcesso());

            AgendamentoIo agendamentoIo = new AgendamentoIo();
            agendamentoIo.setIdProcesso(processo);
            agendamentoIo.setIdEntidadeCefp(entidade);
            agendamentoIo.setCriadoEm(agora);
            agendamentoIo.setUtilizadorRegisto(utilizadorRegisto);
            agendamentoIo.setDatareg(agora);
            agendamentoIo.setTipoAgendamento(TIPO_AGENDAMENTO_VALIDACAO);
            agendamentoIo = agendamentoIoRepository.save(agendamentoIo);
            log.info("Agendamento criado: {}", agendamentoIo.getIdAgendamento());

            acompanhamentoService.criarAcompanhamento(
                    montarAcompanhamento(candidato, processo, entidade, agora)
            );

            enviarEmailRequerente(processo, candidato);

            PedidoRvccResponse response = PedidoRvccMapper.toResponse(candidato, processo, agendamentoIo);
            log.info("Pedido criado com sucesso: {}", response);
            return response;
        } catch (PedidoRvccInvalidoException e) {
            log.warn("Erro de validacao no pedido: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro interno ao criar pedido: ", e);
            throw e;
        }
    }

    private Candidato criarCandidato(
            PedidoRvccRequest request,
            String utilizadorRegisto,
            LocalDateTime agora
    ) {
        validarObrigatorio(request.tipoDocumento(), "tipo_documento");
        validarObrigatorio(request.nif(), "nif");

        DadosCandidato dados = consolidarDados(request);
        validarCamposFinais(dados);
        validarTelemovel(dados.telemovel());
        validarDatas(dados.dataEmissao(), dados.dataValidade(), dados.dataNascimento());

        Integer idadeCalculada = calcularIdade(dados.dataNascimento());
        validarIdade(idadeCalculada);

        Candidato candidato = new Candidato();
        preencherCandidato(candidato, dados, idadeCalculada, utilizadorRegisto, agora);
        return candidatoRepository.save(candidato);
    }

    private Entidade obterEntidadeObrigatoria(Integer idEntidade) {
        validarObrigatorio(idEntidade, "id_entidade");
        return entidadeRepository.findById(idEntidade)
                .orElseThrow(() -> new PedidoRvccInvalidoException("A entidade informada nao existe."));
    }

    private void preencherCandidato(
            Candidato candidato,
            DadosCandidato dados,
            Integer idadeCalculada,
            String utilizadorRegisto,
            LocalDateTime agora
    ) {
        candidato.setNumeroDocumento(dados.numeroDocumento());
        candidato.setTipoDocumento(dados.tipoDocumento());
        candidato.setDataEmissaoDocumento(dados.dataEmissao());
        candidato.setValidadeDocumento(dados.dataValidade());
        candidato.setNomeCompleto(dados.nomeCompleto());
        candidato.setDataNascimento(dados.dataNascimento());
        candidato.setNif(dados.nif());
        candidato.setSexo(dados.genero());
        candidato.setNacionalidade(dados.nacionalidade());
        candidato.setIlha(dados.ilha());
        candidato.setConcelho(dados.concelho());
        candidato.setMorada(dados.morada());
        candidato.setEmail(dados.email());
        candidato.setTelefone(dados.telemovel());
        candidato.setSituacaoEmprego(dados.situacaoEmprego());
        candidato.setEntidadeEmpregadora(dados.entidadeEmpregadora());
        candidato.setProfissao(dados.profissao());
        candidato.setHabilitacoesLiterarias(dados.habilitacaoLiteraria());
        candidato.setDisponibilidade(dados.disponibilidadeHorario());
        candidato.setIdade(idadeCalculada);
        candidato.setEstado(ESTADO_ATIVO);
        candidato.setUtilizadorRegisto(utilizadorRegisto);
        candidato.setDataRegisto(agora);
        candidato.setIdPessoa(dados.idPessoa());
    }

    private DadosCandidato consolidarDados(PedidoRvccRequest request) {
        return new DadosCandidato(
                normalizar(request.tipoDocumento()),
                normalizar(request.numeroDocumento()),
                request.dataEmissao(),
                request.dataValidade(),
                normalizar(request.nomeCompleto()),
                request.dataNascimento(),
                normalizar(request.nif()),
                normalizar(request.genero()),
                normalizar(request.nacionalidade()),
                normalizar(request.ilha()),
                normalizar(request.concelho()),
                normalizar(request.morada()),
                normalizar(request.email()),
                normalizar(request.telemovel()),
                normalizar(request.situacaoEmprego()),
                normalizar(request.entidadeEmpregadora()),
                normalizar(request.profissao()),
                normalizar(request.habilitacaoLiteraria()),
                normalizar(request.disponibilidadeHorario()),
                request.idPessoa()
        );
    }

    private void validarCamposFinais(DadosCandidato dados) {
        validarObrigatorio(dados.tipoDocumento(), "tipo_documento");
        validarObrigatorio(dados.numeroDocumento(), "numero_documento");
        validarObrigatorio(dados.nomeCompleto(), "nome_completo");
        validarObrigatorio(dados.dataNascimento(), "data_nascimento");
        validarObrigatorio(dados.dataEmissao(), "data_emissao");
        validarObrigatorio(dados.dataValidade(), "data_validade");
        validarObrigatorio(dados.nif(), "nif");
        validarObrigatorio(dados.genero(), "genero");
        validarObrigatorio(dados.nacionalidade(), "nacionalidade");
        validarObrigatorio(dados.ilha(), "ilha");
        validarObrigatorio(dados.concelho(), "concelho");
        validarObrigatorio(dados.morada(), "morada");
        validarObrigatorio(dados.email(), "email");
        validarObrigatorio(dados.telemovel(), "telemovel");
        validarObrigatorio(dados.situacaoEmprego(), "situacao_emprego");
        validarObrigatorio(dados.habilitacaoLiteraria(), "habilitacao_literaria");
        validarObrigatorio(dados.disponibilidadeHorario(), "disponibilidade_horario");
    }

    private void validarTelemovel(String telemovel) {
        if (telemovel == null || !telemovel.matches("\\d{7}")) {
            throw new PedidoRvccInvalidoException(
                    "O campo \"Telemovel\" deve aceitar apenas valores numericos inteiros com exatamente 7 caracteres."
            );
        }
    }

    private void validarIdade(Integer idade) {
        if (idade == null) {
            throw new PedidoRvccInvalidoException(MENSAGEM_DATA_NASCIMENTO_INVALIDA);
        }

        if (idade <= 25) {
            throw new PedidoRvccInvalidoException(MENSAGEM_IDADE_INVALIDA);
        }
    }

    private void validarDatas(LocalDate dataEmissao, LocalDate dataValidade, LocalDate dataNascimento) {
        LocalDate hoje = LocalDate.now();

        if (dataEmissao.isAfter(hoje)) {
            throw new PedidoRvccInvalidoException(MENSAGEM_DATA_EMISSAO_INVALIDA);
        }

        if (dataValidade.isBefore(hoje)) {
            throw new PedidoRvccInvalidoException(MENSAGEM_DATA_VALIDADE_INVALIDA);
        }

        if (!dataEmissao.isBefore(dataValidade)) {
            throw new PedidoRvccInvalidoException(MENSAGEM_INTERVALO_DATA_INVALIDO);
        }

        if (!dataNascimento.isBefore(hoje)) {
            throw new PedidoRvccInvalidoException(MENSAGEM_DATA_NASCIMENTO_INVALIDA);
        }
    }

    private Integer calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    private String gerarNumeroProcesso() {
        for (int tentativa = 0; tentativa < 20; tentativa++) {
            String numero = String.valueOf(ThreadLocalRandom.current().nextInt(100_000_000, 1_000_000_000));
            if (!processoRvccRepository.existsByNumProcesso(numero)) {
                return numero;
            }
        }

        throw new PedidoRvccInvalidoException(
                "Nao foi possivel gerar um numero de processo unico. Tente novamente."
        );
    }

    private void validarObrigatorio(Object value, String campo) {
        if (value == null) {
            throw new PedidoRvccInvalidoException(
                    "O campo \"" + campo + "\" e de preenchimento obrigatorio."
            );
        }

        if (value instanceof String texto && !StringUtils.hasText(texto)) {
            throw new PedidoRvccInvalidoException(
                    "O campo \"" + campo + "\" e de preenchimento obrigatorio."
            );
        }
    }

    private String normalizar(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private AcompanhamentoDTO montarAcompanhamento(
            Candidato candidato,
            ProcessoRvcc processo,
            Entidade entidade,
            LocalDateTime agora
    ) {
        Map<String, String> detalhes = new LinkedHashMap<>();
        detalhes.put("Numero Processp", processo.getNumProcesso());
        detalhes.put("Numero Documento", candidato.getNumeroDocumento());
        detalhes.put("Nome Candidato", candidato.getNomeCompleto());
        detalhes.put("Estado Processo", processo.getEstado());

        if (entidade != null && StringUtils.hasText(entidade.getDesignacaoComercial())) {
            detalhes.put("Entidade", entidade.getDesignacaoComercial());
        }

        List<AcompanhamentoDTO.Evento> eventos = new ArrayList<>();
        eventos.add(new AcompanhamentoDTO.Evento(
                "Pedido RVCC criado",
                "Pedido submetido e enviado para acompanhamento.",
                agora,
                detalhes
        ));

        return new AcompanhamentoDTO(
                processo.getNumProcesso(),
                "certificacao_rvcc",
                candidato.getIdPessoa(),
                entidade != null ? entidade.getNif() : null,
                "CERTIFICAO_RVCC",
                "Pedido RVCC",
                10,
                entidade != null ? entidade.getDesignacaoComercial() : null,
                "Pedido RVCC submetido com sucesso.",
                null,
                processo.getDataSubmissao(),
                null,
                "Candidatura",
                "EM_PROGRESSO",
                "em_processo",
                detalhes,
                List.of(),
                eventos,
                List.of(),
                List.of()
        );
    }

    private record DadosCandidato(
            String tipoDocumento,
            String numeroDocumento,
            LocalDate dataEmissao,
            LocalDate dataValidade,
            String nomeCompleto,
            LocalDate dataNascimento,
            String nif,
            String genero,
            String nacionalidade,
            String ilha,
            String concelho,
            String morada,
            String email,
            String telemovel,
            String situacaoEmprego,
            String entidadeEmpregadora,
            String profissao,
            String habilitacaoLiteraria,
            String disponibilidadeHorario,
            Integer idPessoa
    ) {
    }

    public void enviarEmailRequerente(ProcessoRvcc processo, Candidato candidato) {
        TNotificacaoConfigEmail configEmail = notificationService.loadConfigNotification(
                "SOLICITACAO_RVCC",
                null,
                null,
                "certificacao_evcc"
        );

        if (configEmail == null) {
            throw new IllegalStateException("Configuracao de email com o codigo [SOLICITACAO_RVCC] nao existe.");
        }

        String emailRequerente = candidato.getEmail();
        String nomeRequerente = candidato.getNomeCompleto();
        String nomeEntidade = candidato.getEntidadeEmpregadora();
        String decoded = configEmail.getMensagem() != null
                ? HtmlUtils.htmlUnescape(configEmail.getMensagem())
                : "";
        String mensagem = decoded
                .replace("[NOME_COMPLETO]", nomeRequerente)
                .replace("[NOME_ENTIDADE]", nomeEntidade != null ? nomeEntidade : "")
                .replace("[NUMERO_PROCESSO]", processo.getNumProcesso());

        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setAppName("certificacao_rvcc");
        dto.setAssunto(configEmail.getAssunto());
        dto.setMensagem(mensagem);
        dto.setIdProcesso(processo.getIdProcesso() != null ? processo.getIdProcesso().toString() : "");
        dto.setTipoProcesso("pedido_rvcc");
        dto.setIdRelacao(processo.getNumProcesso());
        dto.setTipoRelacao("pedido");
        dto.setEmail(emailRequerente);
        dto.setIsAlert("NAO");

        notificationService.enviarEmail(dto);
    }
}

