package cv.dge.dge_api_rvcc.domain.integration.rvcc.business;

import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.AtividadeProfissionalRvccRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ConhecimentoRvccRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ImportacaoQualificacoesRvccRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.ItemCodigoDenominacaoRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.QualificacaoRvccItemRequest;
import cv.dge.dge_api_rvcc.application.integration.rvcc.dto.request.UnidadeCompetenciaRvccRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Slf4j
public class QualificacaoRvccImportacaoBusImpl implements QualificacaoRvccImportacaoBus {

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

        log.info("Iniciando importacao RVCC com {} qualificacao(oes).", request.data().size());
        Map<String, QualificacaoProfissional> qualificacaoCache = new HashMap<>();

        for (QualificacaoRvccItemRequest item : request.data()) {
            validarItem(item);
            boolean ativa = qualificacaoAtiva(item.estadoQualificacao());
            QualificacaoProfissional qualificacao = obterOuCriarQualificacao(item, qualificacaoCache, ativa);

            if (!ativa) {
                desativarUnidadesEAtividadesDaQualificacao(qualificacao);
                log.info(
                        "Qualificacao RVCC marcada como inativa: id={}, codigo={}, estado recebido={}",
                        qualificacao.getIdQualificacao(),
                        qualificacao.getCodigoCnq(),
                        normalizar(item.estadoQualificacao())
                );
            }

            processarUnidadesCompetencia(item, qualificacao, ativa);
        }

        log.info("Importacao RVCC concluida com sucesso.");
        return request;
    }

    private QualificacaoProfissional obterOuCriarQualificacao(
            QualificacaoRvccItemRequest item,
            Map<String, QualificacaoProfissional> qualificacaoCache,
            boolean ativa
    ) {
        Integer idQualificacaoExistente = item.idQualificacao();
        String codigoQualificacao = normalizar(item.codigoQualif());
        String selfidQp = resolverSelfidQp(item);
        String cacheKey = construirChaveCache(idQualificacaoExistente, selfidQp, codigoQualificacao);

        QualificacaoProfissional qualificacao = qualificacaoCache.get(cacheKey);
        if (qualificacao != null) {
            qualificacao.setAtivo(ativa);
            qualificacao.setIdReferencial(item.idReferencial());
            return qualificacaoRepository.save(qualificacao);
        }

        if (idQualificacaoExistente != null) {
            qualificacao = qualificacaoRepository.findById(idQualificacaoExistente).orElse(null);
        }

        if (qualificacao == null && selfidQp != null) {
            qualificacao = qualificacaoRepository.findBySelfidQp(selfidQp).orElse(null);
        }

        if (qualificacao == null && codigoQualificacao != null) {
            qualificacao = qualificacaoRepository.findByCodigoCnq(codigoQualificacao).orElse(null);
        }

        if (qualificacao == null) {
            qualificacao = new QualificacaoProfissional();
            qualificacao.setSelfidQp(selfidQp);
            qualificacao.setCodigoCnq(codigoQualificacao);
            qualificacao.setIdReferencial(item.idReferencial());
            qualificacao.setDenominacao(normalizar(item.denominacaoQualif()));
            qualificacao.setFamiliaProfissional(primeiroNaoVazio(
                    normalizar(item.denominacaoFamilia()),
                    normalizar(item.codigoFamilia())
            ));
            qualificacao.setNivelQnq(item.nivel());
            qualificacao.setAtivo(ativa);
            qualificacao = qualificacaoRepository.save(qualificacao);
            log.info(
                    "Qualificacao RVCC criada: id={}, codigo={}, selfid_qp={}",
                    qualificacao.getIdQualificacao(),
                    qualificacao.getCodigoCnq(),
                    qualificacao.getSelfidQp()
            );
        } else {
            log.info(
                    "Qualificacao RVCC existente reutilizada: id={}, codigo={}, selfid_qp={}",
                    qualificacao.getIdQualificacao(),
                    qualificacao.getCodigoCnq(),
                    qualificacao.getSelfidQp()
            );
            qualificacao.setAtivo(ativa);
            qualificacao.setIdReferencial(item.idReferencial());
            qualificacao = qualificacaoRepository.save(qualificacao);
        }

        colocarNoCache(qualificacaoCache, qualificacao, idQualificacaoExistente, selfidQp, codigoQualificacao);
        return qualificacao;
    }

    private void processarUnidadesCompetencia(
            QualificacaoRvccItemRequest item,
            QualificacaoProfissional qualificacao,
            boolean ativa
    ) {
        for (UnidadeCompetenciaRvccRequest unidadeRequest : safeList(item.unidadesCompetencia())) {
            validarUnidadeCompetencia(unidadeRequest, item.codigoQualif());

            String codigoUc = normalizar(unidadeRequest.codigo());
            UnidadeCompetencia unidade = obterOuCriarUnidadeCompetencia(
                    qualificacao,
                    unidadeRequest,
                    codigoUc,
                    item.idReferencial()
            );

            unidade.setIdQualificacao(qualificacao);
            unidade.setCodigoUc(codigoUc);
            unidade.setIdUcIntegracao(unidadeRequest.id());
            unidade.setIdReferencial(item.idReferencial());
            unidade.setDenominacao(normalizar(unidadeRequest.denominacao()));
            unidade.setCargaHoraria(parseInteger(unidadeRequest.cargaHorariaModulo(), "cargaHorariaModulo", codigoUc));
            unidade.setCodigoModuloFormativo(normalizar(unidadeRequest.codigoModulo()));
            unidade.setDenominacaoMf(normalizar(unidadeRequest.denominacaoModulo()));
            unidade.setAtivo(ativa);
            unidade = unidadeCompetenciaRepository.save(unidade);
            log.info(
                    "UC gravada: id_uc={}, id_qualificacao={}, codigo_uc={}, ativo={}",
                    unidade.getIdUc(),
                    qualificacao.getIdQualificacao(),
                    unidade.getCodigoUc(),
                    unidade.getAtivo()
            );

            processarAtividades(unidadeRequest, unidade, ativa);
        }
    }

    private UnidadeCompetencia obterOuCriarUnidadeCompetencia(
            QualificacaoProfissional qualificacao,
            UnidadeCompetenciaRvccRequest unidadeRequest,
            String codigoUc,
            Integer idReferencial
    ) {
        if (unidadeRequest.id() != null) {
            UnidadeCompetencia unidade = unidadeCompetenciaRepository
                    .findByIdQualificacao_IdQualificacaoAndIdReferencialAndIdUcIntegracao(
                            qualificacao.getIdQualificacao(),
                            idReferencial,
                            unidadeRequest.id()
                    )
                    .orElse(null);

            if (unidade != null) {
                return unidade;
            }
        }

        return unidadeCompetenciaRepository
                .findByIdQualificacao_IdQualificacaoAndIdReferencialAndCodigoUc(
                        qualificacao.getIdQualificacao(),
                        idReferencial,
                        codigoUc
                )
                .orElseGet(UnidadeCompetencia::new);
    }

    private void processarAtividades(
            UnidadeCompetenciaRvccRequest unidadeRequest,
            UnidadeCompetencia unidade,
            boolean ativa
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
            atividade.setAtivo(ativa);
            atividade = atividadeUnidadeCompetenciaRepository.save(atividade);
            log.info(
                    "Atividade gravada: id_atividade={}, id_uc={}, codigo_atividade={}, ativo={}",
                    atividade.getIdAtividade(),
                    unidade.getIdUc(),
                    atividade.getCodigoAtividade(),
                    atividade.getAtivo()
            );
        }
    }

    private void desativarUnidadesEAtividadesDaQualificacao(QualificacaoProfissional qualificacao) {
        List<UnidadeCompetencia> unidades = unidadeCompetenciaRepository
                .findAllByIdQualificacao_IdQualificacao(qualificacao.getIdQualificacao());

        for (UnidadeCompetencia unidade : unidades) {
            unidade.setAtivo(Boolean.FALSE);

            List<AtividadeUnidadeCompetencia> atividades = atividadeUnidadeCompetenciaRepository
                    .findAllByUnidadeCompetencia_IdUc(unidade.getIdUc());
            for (AtividadeUnidadeCompetencia atividade : atividades) {
                atividade.setAtivo(Boolean.FALSE);
            }
            atividadeUnidadeCompetenciaRepository.saveAll(atividades);
        }

        unidadeCompetenciaRepository.saveAll(unidades);
        log.info(
                "UC/AP desativadas para a qualificacao RVCC: id_qualificacao={}, total_uc={}",
                qualificacao.getIdQualificacao(),
                unidades.size()
        );
    }

    private void validarItem(QualificacaoRvccItemRequest item) {
        if (item == null) {
            throw new ImportacaoInvalidaException("Foi encontrada uma qualificacao nula dentro de 'data'.");
        }

        if (!StringUtils.hasText(item.codigoQualif())) {
            throw new ImportacaoInvalidaException("Toda qualificacao deve conter 'codigoQualif'.");
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
            Integer idQualificacao,
            String selfidQp,
            String codigoQualificacao
    ) {
        cache.put(construirChaveCache(idQualificacao, selfidQp, codigoQualificacao), qualificacao);

        if (idQualificacao != null) {
            cache.put("ID:" + idQualificacao, qualificacao);
        }

        if (selfidQp != null) {
            cache.put("SELF:" + selfidQp, qualificacao);
        }

        if (codigoQualificacao != null) {
            cache.put("COD:" + codigoQualificacao, qualificacao);
        }
    }

    private String construirChaveCache(Integer idQualificacao, String selfidQp, String codigoQualificacao) {
        if (idQualificacao != null) {
            return "ID:" + idQualificacao;
        }

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
