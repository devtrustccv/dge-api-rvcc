package cv.dge.dge_api_rvcc.service;

import cv.dge.dge_api_rvcc.dto.EntidadeImportacaoItemRequest;
import cv.dge.dge_api_rvcc.dto.EntidadeImportacaoResultadoResponse;
import cv.dge.dge_api_rvcc.dto.ImportacaoEntidadesRequest;
import cv.dge.dge_api_rvcc.dto.ImportacaoEntidadesResponse;
import cv.dge.dge_api_rvcc.dto.QualificacaoImportacaoRequest;
import cv.dge.dge_api_rvcc.entity.Entidade;
import cv.dge.dge_api_rvcc.entity.EntidadeQualificacao;
import cv.dge.dge_api_rvcc.entity.EntidadeQualificacaoId;
import cv.dge.dge_api_rvcc.entity.QualificacaoProfissional;
import cv.dge.dge_api_rvcc.exception.ImportacaoInvalidaException;
import cv.dge.dge_api_rvcc.repository.EntidadeQualificacaoRepository;
import cv.dge.dge_api_rvcc.repository.EntidadeRepository;
import cv.dge.dge_api_rvcc.repository.QualificacaoProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EntidadeImportacaoServiceTest {

    @Mock
    private EntidadeRepository entidadeRepository;

    @Mock
    private QualificacaoProfissionalRepository qualificacaoRepository;

    @Mock
    private EntidadeQualificacaoRepository entidadeQualificacaoRepository;

    @InjectMocks
    private EntidadeImportacaoService service;

    @Captor
    private ArgumentCaptor<EntidadeQualificacao> relacaoCaptor;

    private final AtomicInteger qualificacaoIdSequence = new AtomicInteger(100);

    @BeforeEach
    void setup() {
        when(entidadeRepository.save(any(Entidade.class))).thenAnswer(invocation -> {
            Entidade entidade = invocation.getArgument(0);
            if (entidade.getIdEntidade() == null) {
                entidade.setIdEntidade(1);
            }
            return entidade;
        });

        when(qualificacaoRepository.save(any(QualificacaoProfissional.class))).thenAnswer(invocation -> {
            QualificacaoProfissional qualificacao = invocation.getArgument(0);
            if (qualificacao.getIdQualificacao() == null) {
                qualificacao.setIdQualificacao(qualificacaoIdSequence.getAndIncrement());
            }
            return qualificacao;
        });

        when(entidadeQualificacaoRepository.save(any(EntidadeQualificacao.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void deveCriarEntidadeEQualificacoesAoImportarPayload() {
        ImportacaoEntidadesRequest request = new ImportacaoEntidadesRequest(List.of(
                new EntidadeImportacaoItemRequest(
                        "271023007",
                        "ESCOLA HOTELARIA E TURISMO DE CABO VERDE",
                        "SANTIAGO",
                        "PRAIA",
                        238704,
                        "Palmarejo Grande",
                        "017/2024",
                        "Ativo",
                        List.of(
                                new QualificacaoImportacaoRequest("HRT004.A2_5", "365", "Guia de turismo", "Hotelaria, Restauracao e Turismo", "HRT", 5),
                                new QualificacaoImportacaoRequest("HRT002_4", "405", "Cozinha", "Hotelaria, Restauracao e Turismo", "HRT", 4)
                        ),
                        List.of(
                                new QualificacaoImportacaoRequest("HRT009_3", "440", "Padaria e Pastelaria", "Hotelaria, Restauracao e Turismo", "HRT", 3)
                        )
                )
        ));

        when(entidadeRepository.findByNif("271023007")).thenReturn(Optional.empty());
        when(qualificacaoRepository.findByCodigoCnq(anyString())).thenReturn(Optional.empty());
        when(entidadeQualificacaoRepository.findAllByEntidade_IdEntidade(1)).thenReturn(List.of());

        ImportacaoEntidadesResponse response = service.importar(request);

        assertEquals(1, response.totalRecebido());
        assertEquals(1, response.entidadesCriadas());
        assertEquals(0, response.entidadesAtualizadas());
        assertEquals(3, response.qualificacoesCriadas());
        assertEquals(3, response.relacoesCriadas());
        assertEquals(0, response.relacoesAtualizadas());
        assertEquals(0, response.relacoesRemovidas());

        EntidadeImportacaoResultadoResponse resultado = response.resultados().get(0);
        assertEquals("271023007", resultado.nif());
        assertEquals(1, resultado.idEntidade());
        assertTrue(resultado.criada());

        verify(entidadeQualificacaoRepository, times(3)).save(relacaoCaptor.capture());
        List<EntidadeQualificacao> relacoesSalvas = relacaoCaptor.getAllValues();

        assertEquals("Ativo", relacoesSalvas.get(0).getEstadoAcreditacao());
        assertEquals("Ativo", relacoesSalvas.get(1).getEstadoAcreditacao());
        assertEquals("Inativo", relacoesSalvas.get(2).getEstadoAcreditacao());
        assertEquals(new EntidadeQualificacaoId(1, 100), relacoesSalvas.get(0).getId());
    }

    @Test
    void deveRemoverRelacaoNaoEnviadaNoNovoSnapshot() {
        Entidade entidadeExistente = new Entidade();
        entidadeExistente.setIdEntidade(20);
        entidadeExistente.setNif("271023007");
        entidadeExistente.setAtivo(Boolean.TRUE);

        QualificacaoProfissional qualificacaoAntiga = new QualificacaoProfissional();
        qualificacaoAntiga.setIdQualificacao(500);
        qualificacaoAntiga.setCodigoCnq("HRT_OLD");

        EntidadeQualificacao relacaoAntiga = new EntidadeQualificacao();
        relacaoAntiga.setId(new EntidadeQualificacaoId(20, 500));
        relacaoAntiga.setEntidade(entidadeExistente);
        relacaoAntiga.setQualificacao(qualificacaoAntiga);
        relacaoAntiga.setEstadoAcreditacao("Ativo");

        ImportacaoEntidadesRequest request = new ImportacaoEntidadesRequest(List.of(
                new EntidadeImportacaoItemRequest(
                        "271023007",
                        "ESCOLA HOTELARIA E TURISMO DE CABO VERDE",
                        "SANTIAGO",
                        "PRAIA",
                        238704,
                        "Palmarejo Grande",
                        "017/2024",
                        "Ativo",
                        List.of(new QualificacaoImportacaoRequest("HRT_NEW", "700", "Recepcao", "Hotelaria", "HRT", 4)),
                        List.of()
                )
        ));

        when(entidadeRepository.findByNif("271023007")).thenReturn(Optional.of(entidadeExistente));
        when(qualificacaoRepository.findByCodigoCnq("HRT_NEW")).thenReturn(Optional.empty());
        when(entidadeQualificacaoRepository.findAllByEntidade_IdEntidade(20)).thenReturn(List.of(relacaoAntiga));

        ImportacaoEntidadesResponse response = service.importar(request);

        assertEquals(0, response.entidadesCriadas());
        assertEquals(1, response.entidadesAtualizadas());
        assertEquals(1, response.relacoesCriadas());
        assertEquals(1, response.relacoesRemovidas());

        verify(entidadeQualificacaoRepository).deleteAll(List.of(relacaoAntiga));
    }

    @Test
    void deveFalharQuandoHaQualificacaoDuplicadaNaMesmaEntidade() {
        ImportacaoEntidadesRequest request = new ImportacaoEntidadesRequest(List.of(
                new EntidadeImportacaoItemRequest(
                        "271023007",
                        "ESCOLA HOTELARIA E TURISMO DE CABO VERDE",
                        "SANTIAGO",
                        "PRAIA",
                        238704,
                        "Palmarejo Grande",
                        "017/2024",
                        "Ativo",
                        List.of(new QualificacaoImportacaoRequest("HRT004.A2_5", "365", "Guia de turismo", "Hotelaria", "HRT", 5)),
                        List.of(new QualificacaoImportacaoRequest("HRT004.A2_5", "365", "Guia de turismo", "Hotelaria", "HRT", 5))
                )
        ));

        when(entidadeRepository.findByNif("271023007")).thenReturn(Optional.empty());
        when(entidadeQualificacaoRepository.findAllByEntidade_IdEntidade(1)).thenReturn(List.of());

        assertThrows(ImportacaoInvalidaException.class, () -> service.importar(request));
        verify(qualificacaoRepository, never()).save(any(QualificacaoProfissional.class));
    }
}
