package cv.dge.dge_api_rvcc.domain.report.business;

import cv.dge.dge_api_rvcc.application.report.dto.CertificadoQualificacaoProfissionalReportResponse;
import cv.dge.dge_api_rvcc.application.report.dto.EntidadeFormadoraReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.UnidadeCompetenciaReportDto;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.CertificadoQualificacaoProfissionalReportRepository;
import cv.dge.dge_api_rvcc.infrastructure.primary.repository.CertificadoQualificacaoProfissionalReportRepository.DadosCertificado;
import cv.dge.dge_api_rvcc.infrastructure.tertiary.DocRelacaoEntity;
import cv.dge.dge_api_rvcc.infrastructure.tertiary.repository.DocRelacaoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CertificadoQualificacaoProfissionalReportBusImpl implements CertificadoQualificacaoProfissionalReportBus {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String VIA_CERTIFICACAO_RVCC =
            "do processo de Reconhecimento, validacao e certificacao de competencias profissionais";
    private static final String TIPO_RELACAO_LOGOTIPO = "LOGOTIPO_ENTIDADE_RVCC";

    private final CertificadoQualificacaoProfissionalReportRepository repository;
    private final DocRelacaoRepository docRelacaoRepository;
    private final RestTemplate restTemplate;
    private final String linkApiBase;

    public CertificadoQualificacaoProfissionalReportBusImpl(
            CertificadoQualificacaoProfissionalReportRepository repository,
            DocRelacaoRepository docRelacaoRepository,
            @Value("${link.api.base}") String linkApiBase
    ) {
        this.repository = repository;
        this.docRelacaoRepository = docRelacaoRepository;
        this.restTemplate = new RestTemplate();
        this.linkApiBase = linkApiBase;
    }

    public Optional<CertificadoQualificacaoProfissionalReportResponse> obterDados(
            Integer idProcesso,
            Integer idQualificacao
    ) {
        Optional<DadosCertificado> optionalDados = repository.obterDadosCertificado(idProcesso, idQualificacao);

        if (optionalDados.isEmpty()) {
            return Optional.empty();
        }

        DadosCertificado dado = optionalDados.get();
        List<UnidadeCompetenciaReportDto> unidades = repository.obterUnidadesCompetencia(
                dado.idProcesso(),
                dado.qualificacao().idQualificacao()
        );
        LocalDate dataEmissao = LocalDate.now();
        String numeroCertificado = montarNumeroCertificado(dado.numProcesso(), dado.idProcesso(), dataEmissao);
        LocalDate dataAvaliacao = repository.obterDataAvaliacao(
                dado.idProcesso(),
                dado.qualificacao().idQualificacao()
        );

        String logotipoUrl = obterLogotipoEntidadeUrl(dado.idEntidade());
        EntidadeFormadoraReportDto entidadeComLogotipo = new EntidadeFormadoraReportDto(
                dado.entidadeFormadora().nome(),
                dado.entidadeFormadora().numeroAlvara(),
                logotipoUrl
        );

        Map<String, Object> campos = montarCampos(
                dado,
                entidadeComLogotipo,
                unidades,
                dataEmissao,
                dataAvaliacao,
                numeroCertificado
        );

        return Optional.of(new CertificadoQualificacaoProfissionalReportResponse(
                dado.idProcesso(),
                dado.numProcesso(),
                numeroCertificado,
                format(dataEmissao),
                format(dado.dataGeracaoCertificado()),
                format(dataAvaliacao),
                dado.dataFinalizacaoFormacao(),
                VIA_CERTIFICACAO_RVCC,
                codigoContraprovaCertificado(dado.numProcesso(), dado.idProcesso()),
                entidadeComLogotipo.numeroAlvara(),
                dado.candidato(),
                entidadeComLogotipo,
                dado.qualificacao(),
                unidades,
                Collections.emptyList(),
                campos
        ));
    }

    private Map<String, Object> montarCampos(
            DadosCertificado dado,
            EntidadeFormadoraReportDto entidade,
            List<UnidadeCompetenciaReportDto> unidades,
            LocalDate dataEmissao,
            LocalDate dataAvaliacao,
            String numeroCertificado
    ) {
        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("nome", dado.candidato().nome());
        campos.put("naturalidade", dado.candidato().naturalidade());
        campos.put("dataNascimento", dado.candidato().dataNascimento());
        campos.put("documentoIdentificacao", dado.candidato().documentoIdentificacao());
        campos.put("dataFinalizacaoFormacao", dado.dataFinalizacaoFormacao());
        campos.put("entidadeFormadora", entidade.nome());
        campos.put("alvaraEntidadeFormadora", entidade.numeroAlvara());
        campos.put("nomeQualificacao", dado.qualificacao().nome());
        campos.put("nivelQualificacao", dado.qualificacao().nivel());
        campos.put("familiaProfissional", dado.qualificacao().familiaProfissional());
        campos.put("viaCertificacao", VIA_CERTIFICACAO_RVCC);
        campos.put("dataEmissao", format(dataEmissao));
        campos.put("dataGeracaoCertificado", format(dado.dataGeracaoCertificado()));
        campos.put("dataAvaliacao", format(dataAvaliacao));
        campos.put("nomeEntidadeFormadora", entidade.nome());
        campos.put("logotipoEntidadeFormadora", entidade.logotipoUrl());
        campos.put("numeroCertificado", numeroCertificado);
        campos.put("codigoContraprovaCertificado", codigoContraprovaCertificado(dado.numProcesso(), dado.idProcesso()));
        campos.put("codigoContraprovaAlvara", entidade.numeroAlvara());
        return campos;
    }

    private String obterLogotipoEntidadeUrl(Integer idEntidade) {
        if (idEntidade == null) {
            return null;
        }

        System.out.println("DEBUG: Buscando logotipo para entidade: " + idEntidade);
        List<DocRelacaoEntity> docs = docRelacaoRepository
                .findByIdRelacaoAndTipoRelacao(BigDecimal.valueOf(idEntidade), TIPO_RELACAO_LOGOTIPO);
        System.out.println("DEBUG: Encontrados " + docs.size() + " documentos");

        Optional<DocRelacaoEntity> docRelacao = docs.stream()
                .filter(doc -> {
                    System.out.println("DEBUG: Doc id=" + doc.getId() + " estado=" + doc.getEstado() + " path=" + doc.getPath());
                    return "A".equals(doc.getEstado());
                })
                .max(Comparator.comparing(DocRelacaoEntity::getDateCreate));

        if (docRelacao.isEmpty()) {
            System.out.println("DEBUG: Nenhum logotipo encontrado para entidade: " + idEntidade);
            return null;
        }

        String filePath = docRelacao.get().getPath();
        System.out.println("DEBUG: Logotipo path: " + filePath);
        String url = UriComponentsBuilder.fromHttpUrl(linkApiBase)
                .path("/api/documentos/public-url")
                .queryParam("file_path", filePath)
                .toUriString();

        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return null;
            }

            return response.getBody().get("url");
        } catch (RestClientException exception) {
            return null;
        }
    }

    private String montarNumeroCertificado(String numProcesso, Integer idProcesso, LocalDate dataEmissao) {
        String base = primeiroNaoVazio(numProcesso, String.valueOf(idProcesso));
        return base + "-" + dataEmissao.getYear();
    }

    private String codigoContraprovaCertificado(String numProcesso, Integer idProcesso) {
        return primeiroNaoVazio(numProcesso, String.valueOf(idProcesso));
    }

    private String primeiroNaoVazio(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }

        return null;
    }

    private String format(LocalDate value) {
        return value == null ? null : value.format(DATE_FORMAT);
    }
}
