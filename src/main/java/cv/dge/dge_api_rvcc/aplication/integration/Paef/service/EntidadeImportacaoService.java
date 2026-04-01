package cv.dge.dge_api_rvcc.aplication.integration.Paef.service;

import cv.dge.dge_api_rvcc.common.exception.ImportacaoInvalidaException;
import cv.dge.dge_api_rvcc.aplication.integration.Paef.dto.request.EntidadeImportacaoItemRequest;
import cv.dge.dge_api_rvcc.aplication.integration.Paef.dto.request.ImportacaoEntidadesRequest;
import cv.dge.dge_api_rvcc.aplication.integration.Paef.dto.request.QualificacaoImportacaoRequest;
import cv.dge.dge_api_rvcc.infrastructure.primary.client.OrganizacaoApiClient;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.Entidade;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.EntidadeQualificacao;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.EntidadeQualificacaoId;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.QualificacaoProfissional;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.EntidadeQualificacaoRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.EntidadeRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.QualificacaoProfissionalRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class EntidadeImportacaoService {

    private static final String ESTADO_ATIVO = "Ativo";
    private static final String ESTADO_INATIVO = "Inativo";

    private final EntidadeRepository entidadeRepository;
    private final QualificacaoProfissionalRepository qualificacaoRepository;
    private final EntidadeQualificacaoRepository entidadeQualificacaoRepository;
    private final OrganizacaoApiClient organizacaoApiClient;

    @Transactional
    public ImportacaoEntidadesRequest importar(ImportacaoEntidadesRequest request) {
        if (request == null || request.data() == null || request.data().isEmpty()) {
            throw new ImportacaoInvalidaException("O payload deve conter pelo menos uma entidade em 'data'.");
        }

        validarNifsDuplicados(request.data());

        Map<String, QualificacaoProfissional> qualificacaoCache = new HashMap<>();

        for (EntidadeImportacaoItemRequest item : request.data()) {
            validarEntidade(item);
            processarEntidade(item, qualificacaoCache);
        }

        return request;
    }

    private void processarEntidade(
            EntidadeImportacaoItemRequest item,
            Map<String, QualificacaoProfissional> qualificacaoCache
    ) {
        Map<String, QualificacaoComEstado> qualificacoesDesejadas = construirMapaQualificacoes(item);
        Optional<Entidade> entidadeExistente = entidadeRepository.findByNif(normalizar(item.nif()));
        Entidade entidade = entidadeExistente.orElseGet(Entidade::new);
        String idOrganica = organizacaoApiClient.criarOrganizacao(item.designacaoComercial());

        aplicarDadosEntidade(entidade, item, idOrganica);
        entidade = entidadeRepository.save(entidade);

        sincronizarQualificacoes(entidade, qualificacoesDesejadas, qualificacaoCache);
    }

    private void aplicarDadosEntidade(Entidade entidade, EntidadeImportacaoItemRequest item, String idOrganica) {
        entidade.setNif(normalizar(item.nif()));
        entidade.setDesignacaoComercial(normalizar(item.designacaoComercial()));
        entidade.setIlha(normalizar(item.ilha()));
        entidade.setConcelho(normalizar(item.concelho()));
        entidade.setIdConcelho(item.idConcelho());
        entidade.setEndereco(normalizar(item.endereco()));
        entidade.setNumAlvara(normalizar(item.numAlvara()));
        entidade.setEstadoAlvara(normalizar(item.estadoAlvara()));
        entidade.setIdOrganica(normalizar(idOrganica));
        if (entidade.getAtivo() == null) {
            entidade.setAtivo(Boolean.TRUE);
        }
    }

    private void sincronizarQualificacoes(
            Entidade entidade,
            Map<String, QualificacaoComEstado> qualificacoesDesejadas,
            Map<String, QualificacaoProfissional> qualificacaoCache
    ) {
        List<EntidadeQualificacao> relacoesExistentes = entidadeQualificacaoRepository.findAllByEntidade_IdEntidade(entidade.getIdEntidade());
        Map<String, EntidadeQualificacao> relacoesPorCodigo = relacoesExistentes.stream()
                .collect(Collectors.toMap(
                        relacao -> relacao.getQualificacao().getCodigoCnq(),
                        relacao -> relacao
                ));

        for (QualificacaoComEstado item : qualificacoesDesejadas.values()) {
            QualificacaoProfissional qualificacao = obterOuCriarQualificacao(
                    item.qualificacao(),
                    qualificacaoCache,
                    item.qualificacaoAtiva()
            );
            EntidadeQualificacao relacao = relacoesPorCodigo.remove(qualificacao.getCodigoCnq());

            if (relacao == null) {
                relacao = new EntidadeQualificacao();
                relacao.setId(new EntidadeQualificacaoId(entidade.getIdEntidade(), qualificacao.getIdQualificacao()));
                relacao.setEntidade(entidade);
                relacao.setQualificacao(qualificacao);
                relacao.setEstadoAcreditacao(item.estadoAcreditacao());
                entidadeQualificacaoRepository.save(relacao);
                continue;
            }

            boolean alterouEstado = !Objects.equals(relacao.getEstadoAcreditacao(), item.estadoAcreditacao());
            relacao.setEstadoAcreditacao(item.estadoAcreditacao());
            relacao.setQualificacao(qualificacao);

            if (alterouEstado) {
                entidadeQualificacaoRepository.save(relacao);
            }
        }

        List<EntidadeQualificacao> relacoesParaRemover = new ArrayList<>(relacoesPorCodigo.values());
        if (!relacoesParaRemover.isEmpty()) {
            entidadeQualificacaoRepository.deleteAll(relacoesParaRemover);
        }
    }

    private QualificacaoProfissional obterOuCriarQualificacao(
            QualificacaoImportacaoRequest payload,
            Map<String, QualificacaoProfissional> qualificacaoCache,
            boolean qualificacaoAtiva
    ) {
        String codigoCnq = normalizar(payload.codigoCnq());
        QualificacaoProfissional qualificacao = qualificacaoCache.get(codigoCnq);

        if (qualificacao == null) {
            qualificacao = qualificacaoRepository.findByCodigoCnq(codigoCnq).orElse(null);
        }

        if (qualificacao == null) {
            qualificacao = new QualificacaoProfissional();
            qualificacao.setCodigoCnq(codigoCnq);
        }

        qualificacao.setSelfidQp(normalizar(payload.selfidQp()));
        qualificacao.setDenominacao(normalizar(payload.denominacao()));
        qualificacao.setFamiliaProfissional(normalizar(payload.familiaProfissional()));
        qualificacao.setNivelQnq(payload.nivelQnq());
        qualificacao.setAtivo(qualificacaoAtiva);

        qualificacao = qualificacaoRepository.save(qualificacao);
        qualificacaoCache.put(codigoCnq, qualificacao);
        return qualificacao;
    }

    private Map<String, QualificacaoComEstado> construirMapaQualificacoes(EntidadeImportacaoItemRequest item) {
        Map<String, QualificacaoComEstado> qualificacoes = new LinkedHashMap<>();
        adicionarQualificacoes(qualificacoes, item.qualificacoesAtivas(), ESTADO_ATIVO, item.nif());
        adicionarQualificacoes(qualificacoes, item.qualificacoesInativas(), ESTADO_INATIVO, item.nif());
        return qualificacoes;
    }

    private void adicionarQualificacoes(
            Map<String, QualificacaoComEstado> qualificacoes,
            List<QualificacaoImportacaoRequest> itens,
            String estadoAcreditacao,
            String nif
    ) {
        for (QualificacaoImportacaoRequest item : safeList(itens)) {
            validarQualificacao(item, nif);
            String codigoCnq = normalizar(item.codigoCnq());
            QualificacaoComEstado anterior = qualificacoes.putIfAbsent(
                    codigoCnq,
                    new QualificacaoComEstado(item, estadoAcreditacao, ESTADO_ATIVO.equals(estadoAcreditacao))
            );

            if (anterior != null) {
                throw new ImportacaoInvalidaException(
                        "A qualificacao '" + codigoCnq + "' foi enviada mais de uma vez para o NIF '" + normalizar(nif) + "'."
                );
            }
        }
    }

    private void validarNifsDuplicados(List<EntidadeImportacaoItemRequest> entidades) {
        Set<String> nifs = new LinkedHashSet<>();

        for (EntidadeImportacaoItemRequest item : entidades) {
            if (item == null || !StringUtils.hasText(item.nif())) {
                throw new ImportacaoInvalidaException("Cada entidade enviada deve conter um NIF.");
            }

            String nif = normalizar(item.nif());
            if (!nifs.add(nif)) {
                throw new ImportacaoInvalidaException("O NIF '" + nif + "' foi enviado mais de uma vez no mesmo payload.");
            }
        }
    }

    private void validarEntidade(EntidadeImportacaoItemRequest item) {
        if (item == null) {
            throw new ImportacaoInvalidaException("Foi encontrada uma entidade nula dentro de 'data'.");
        }

        if (!StringUtils.hasText(item.designacaoComercial())) {
            throw new ImportacaoInvalidaException("A entidade com NIF '" + normalizar(item.nif()) + "' deve ter 'designacao_comercial'.");
        }
    }

    private void validarQualificacao(QualificacaoImportacaoRequest item, String nif) {
        if (item == null) {
            throw new ImportacaoInvalidaException(
                    "Foi encontrada uma qualificacao nula para o NIF '" + normalizar(nif) + "'."
            );
        }

        if (!StringUtils.hasText(item.codigoCnq())) {
            throw new ImportacaoInvalidaException(
                    "Toda qualificacao deve conter 'codigo_cnq' para o NIF '" + normalizar(nif) + "'."
            );
        }

        if (!StringUtils.hasText(item.denominacao())) {
            throw new ImportacaoInvalidaException(
                    "A qualificacao '" + normalizar(item.codigoCnq()) + "' deve conter 'denominacao'."
            );
        }
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? Collections.emptyList() : values;
    }

    private String normalizar(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private record QualificacaoComEstado(
            QualificacaoImportacaoRequest qualificacao,
            String estadoAcreditacao,
            boolean qualificacaoAtiva
    ) {
    }
}

