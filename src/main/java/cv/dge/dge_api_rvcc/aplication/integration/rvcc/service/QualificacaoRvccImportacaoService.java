package cv.dge.dge_api_rvcc.aplication.integration.rvcc.service;

import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.AtividadeProfissionalRvccRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.ConhecimentoRvccRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.ItemCodigoDenominacaoRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.QualificacaoRvccItemRequest;
import cv.dge.dge_api_rvcc.aplication.integration.rvcc.dto.request.UnidadeCompetenciaRvccRequest;
import cv.dge.dge_api_rvcc.common.exception.ImportacaoInvalidaException;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.AtividadeUnidadeCompetencia;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.QualificacaoProfissional;
import cv.dge.dge_api_rvcc.infrastructure.primary.entity.UnidadeCompetencia;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.AtividadeUnidadeCompetenciaRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.QualificacaoProfissionalRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.UnidadeCompetenciaRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class QualificacaoRvccImportacaoService {

    private static final String ESTADO_ATIVO_SIGLA = "A";
    private static final String ESTADO_ATIVO = "ATIVO";

    private final QualificacaoProfissionalRepository qualificacaoRepository;
    private final UnidadeCompetenciaRepository unidadeCompetenciaRepository;
    private final AtividadeUnidadeCompetenciaRepository atividadeUnidadeCompetenciaRepository;

    @Transactional
    public ImportacaoQualificacoesRvccRequest importar(ImportacaoQualificacoesRvccRequest request) {
        if (request == null || request.data() == null || request.data().isEmpty()) {
            throw new ImportacaoInvalidaException("O payload deve conter pelo menos uma qualificacao em 'data'.");
        }

        Map<String, QualificacaoProfissional> qualificacaoCache = new HashMap<>();

        for (QualificacaoRvccItemRequest item : request.data()) {
            validarItem(item);

            if (!qualificacaoAtiva(item.estadoQualificacao())) {
                continue;
            }

            QualificacaoProfissional qualificacao = obterOuCriarQualificacao(item, qualificacaoCache);
            processarUnidadesCompetencia(item, qualificacao);
        }

        return request;
    }

    private QualificacaoProfissional obterOuCriarQualificacao(
            QualificacaoRvccItemRequest item,
            Map<String, QualificacaoProfissional> qualificacaoCache
    ) {
        String codigoQualificacao = normalizar(item.codigoQualif());
        String selfidQp = resolverSelfidQp(item);
        String cacheKey = construirChaveCache(selfidQp, codigoQualificacao);

        QualificacaoProfissional qualificacao = qualificacaoCache.get(cacheKey);
        if (qualificacao != null) {
            return qualificacao;
        }

        if (selfidQp != null) {
            qualificacao = qualificacaoRepository.findBySelfidQp(selfidQp).orElse(null);
        }

        if (qualificacao == null && codigoQualificacao != null) {
            qualificacao = qualificacaoRepository.findByCodigoCnq(codigoQualificacao).orElse(null);
        }

        if (qualificacao == null) {
            qualificacao = new QualificacaoProfissional();
            qualificacao.setSelfidQp(selfidQp);
            qualificacao.setCodigoCnq(codigoQualificacao);
            qualificacao.setDenominacao(normalizar(item.denominacaoQualif()));
            qualificacao.setFamiliaProfissional(primeiroNaoVazio(
                    normalizar(item.denominacaoFamilia()),
                    normalizar(item.codigoFamilia())
            ));
            qualificacao.setNivelQnq(item.nivel());
            qualificacao.setAtivo(Boolean.TRUE);
            qualificacao = qualificacaoRepository.save(qualificacao);
        }

        colocarNoCache(qualificacaoCache, qualificacao, selfidQp, codigoQualificacao);
        return qualificacao;
    }

    private void processarUnidadesCompetencia(
            QualificacaoRvccItemRequest item,
            QualificacaoProfissional qualificacao
    ) {
        for (UnidadeCompetenciaRvccRequest unidadeRequest : safeList(item.unidadesCompetencia())) {
            validarUnidadeCompetencia(unidadeRequest, item.codigoQualif());

            String codigoUc = normalizar(unidadeRequest.codigo());
            UnidadeCompetencia unidade = unidadeCompetenciaRepository
                    .findByIdQualificacao_IdQualificacaoAndCodigoUc(qualificacao.getIdQualificacao(), codigoUc)
                    .orElseGet(UnidadeCompetencia::new);

            unidade.setIdQualificacao(qualificacao);
            unidade.setCodigoUc(codigoUc);
            unidade.setDenominacao(normalizar(unidadeRequest.denominacao()));
            unidade.setCargaHoraria(parseInteger(unidadeRequest.cargaHorariaModulo(), "cargaHorariaModulo", codigoUc));
            unidade.setCodigoModuloFormativo(normalizar(unidadeRequest.codigoModulo()));
            unidade.setDenominacaoMf(normalizar(unidadeRequest.denominacaoModulo()));
            unidade.setAtivo(Boolean.TRUE);
            unidade = unidadeCompetenciaRepository.save(unidade);

            processarAtividades(unidadeRequest, unidade);
        }
    }

    private void processarAtividades(
            UnidadeCompetenciaRvccRequest unidadeRequest,
            UnidadeCompetencia unidade
    ) {
        for (AtividadeProfissionalRvccRequest atividadeRequest : safeList(unidadeRequest.atividadesProfissionais())) {
            validarAtividade(atividadeRequest, unidadeRequest.codigo());

            String codigoAtividade = normalizar(atividadeRequest.codigo());
            AtividadeUnidadeCompetencia atividade = atividadeUnidadeCompetenciaRepository
                    .findByUnidadeCompetencia_IdUcAndCodigoAtividade(unidade.getIdUc(), codigoAtividade)
                    .orElseGet(AtividadeUnidadeCompetencia::new);

            atividade.setUnidadeCompetencia(unidade);
            atividade.setCodigoAtividade(codigoAtividade);
            atividade.setDescricao(normalizar(atividadeRequest.descricao()));
            atividade.setPonderacao(atividadeRequest.ponderacao());
            atividade.setRequisitos(formatarItensCodigoDenominacao(atividadeRequest.requisitos()));
            atividade.setConhecimentos(formatarConhecimentos(atividadeRequest.conhecimentos()));
            atividade.setAtivo(Boolean.TRUE);
            atividadeUnidadeCompetenciaRepository.save(atividade);
        }
    }

    private void validarItem(QualificacaoRvccItemRequest item) {
        if (item == null) {
            throw new ImportacaoInvalidaException("Foi encontrada uma qualificacao nula dentro de 'data'.");
        }

        if (!qualificacaoAtiva(item.estadoQualificacao())) {
            return;
        }

        if (!StringUtils.hasText(item.codigoQualif())) {
            throw new ImportacaoInvalidaException("Toda qualificacao ativa deve conter 'codigoQualif'.");
        }

        if (!StringUtils.hasText(item.denominacaoQualif())) {
            throw new ImportacaoInvalidaException(
                    "A qualificacao '" + normalizar(item.codigoQualif()) + "' deve conter 'denominacaoQualif'."
            );
        }
    }

    private void validarUnidadeCompetencia(UnidadeCompetenciaRvccRequest item, String codigoQualificacao) {
        if (item == null) {
            throw new ImportacaoInvalidaException(
                    "Foi encontrada uma unidade de competencia nula para a qualificacao '" + normalizar(codigoQualificacao) + "'."
            );
        }

        if (!StringUtils.hasText(item.codigo())) {
            throw new ImportacaoInvalidaException(
                    "Toda unidade de competencia da qualificacao '" + normalizar(codigoQualificacao) + "' deve conter 'codigo'."
            );
        }

        if (!StringUtils.hasText(item.denominacao())) {
            throw new ImportacaoInvalidaException(
                    "A unidade de competencia '" + normalizar(item.codigo()) + "' deve conter 'denominacao'."
            );
        }
    }

    private void validarAtividade(AtividadeProfissionalRvccRequest item, String codigoUc) {
        if (item == null) {
            throw new ImportacaoInvalidaException(
                    "Foi encontrada uma atividade profissional nula para a UC '" + normalizar(codigoUc) + "'."
            );
        }

        if (!StringUtils.hasText(item.codigo())) {
            throw new ImportacaoInvalidaException(
                    "Toda atividade profissional da UC '" + normalizar(codigoUc) + "' deve conter 'codigo'."
            );
        }
    }

    private boolean qualificacaoAtiva(String estadoQualificacao) {
        String estadoNormalizado = normalizar(estadoQualificacao);
        return ESTADO_ATIVO_SIGLA.equalsIgnoreCase(estadoNormalizado)
                || ESTADO_ATIVO.equalsIgnoreCase(estadoNormalizado);
    }

    private String resolverSelfidQp(QualificacaoRvccItemRequest item) {
        String selfId = normalizar(item.selfId());
        if (selfId != null) {
            return selfId;
        }

        if (item.idQualificacao() != null) {
            return String.valueOf(item.idQualificacao());
        }

        return null;
    }

    private String formatarItensCodigoDenominacao(List<ItemCodigoDenominacaoRequest> itens) {
        Set<String> valores = new LinkedHashSet<>();

        for (ItemCodigoDenominacaoRequest item : safeList(itens)) {
            if (item == null) {
                continue;
            }

            String valor = formatarCodigoDenominacao(normalizar(item.codigo()), normalizar(item.denominacao()));
            if (valor != null) {
                valores.add(valor);
            }
        }

        return juntarPorPontoVirgula(valores);
    }

    private String formatarConhecimentos(List<ConhecimentoRvccRequest> conhecimentos) {
        Set<String> valores = new LinkedHashSet<>();

        for (ConhecimentoRvccRequest conhecimento : safeList(conhecimentos)) {
            if (conhecimento == null) {
                continue;
            }

            String codigo = primeiroNaoVazio(
                    normalizar(conhecimento.codigo()),
                    normalizar(conhecimento.codigoUf()),
                    normalizar(conhecimento.codigoMf())
            );
            String denominacao = primeiroNaoVazio(
                    normalizar(conhecimento.denominacao()),
                    normalizar(conhecimento.denominacaoUf()),
                    normalizar(conhecimento.denominacaoMf())
            );
            String valor = formatarCodigoDenominacao(codigo, denominacao);
            if (valor != null) {
                valores.add(valor);
            }
        }

        return juntarPorPontoVirgula(valores);
    }

    private String formatarCodigoDenominacao(String codigo, String denominacao) {
        if (codigo != null && denominacao != null) {
            return codigo + " - " + denominacao;
        }

        return primeiroNaoVazio(codigo, denominacao);
    }

    private String juntarPorPontoVirgula(Set<String> valores) {
        if (valores.isEmpty()) {
            return null;
        }

        return valores.stream().collect(Collectors.joining("; "));
    }

    private Integer parseInteger(String value, String fieldName, String codigoUc) {
        String normalizado = normalizar(value);
        if (normalizado == null) {
            return null;
        }

        try {
            return Integer.valueOf(normalizado);
        } catch (NumberFormatException exception) {
            throw new ImportacaoInvalidaException(
                    "A UC '" + normalizar(codigoUc) + "' contem o campo '" + fieldName + "' com valor invalido."
            );
        }
    }

    private void colocarNoCache(
            Map<String, QualificacaoProfissional> cache,
            QualificacaoProfissional qualificacao,
            String selfidQp,
            String codigoQualificacao
    ) {
        cache.put(construirChaveCache(selfidQp, codigoQualificacao), qualificacao);

        if (selfidQp != null) {
            cache.put("SELF:" + selfidQp, qualificacao);
        }

        if (codigoQualificacao != null) {
            cache.put("COD:" + codigoQualificacao, qualificacao);
        }
    }

    private String construirChaveCache(String selfidQp, String codigoQualificacao) {
        if (selfidQp != null) {
            return "SELF:" + selfidQp;
        }

        return "COD:" + codigoQualificacao;
    }

    private String primeiroNaoVazio(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }

        return null;
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
}
