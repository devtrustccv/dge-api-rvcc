package cv.dge.dge_api_rvcc.infrastructure.primary.repository;

import cv.dge.dge_api_rvcc.application.report.dto.CandidatoReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.EntidadeFormadoraReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.QualificacaoReportDto;
import cv.dge.dge_api_rvcc.application.report.dto.UnidadeCompetenciaReportDto;
import cv.dge.dge_api_rvcc.application.geografia.service.GeografiaService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class CertificadoQualificacaoProfissionalReportRepositoryImpl
        implements CertificadoQualificacaoProfissionalReportRepository {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final GeografiaService geografiaService;

    public CertificadoQualificacaoProfissionalReportRepositoryImpl(
            @Qualifier("primaryDataSource") DataSource dataSource,
            GeografiaService geografiaService
    ) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.geografiaService = geografiaService;
    }

    @Override
    public Optional<DadosCertificado> obterDadosCertificado(Integer idProcesso, Integer idQualificacao) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idProcesso", idProcesso)
                .addValue("idQualificacao", idQualificacao, Types.INTEGER);

        List<DadosCertificado> dados = jdbcTemplate.query("""
                SELECT
                    p.id_processo,
                    p.num_processo,
                    p.data_conclusao,
                    p.data_submissao,
                    p.data_geracao_certificado,
                    c.nome_completo,
                    c.concelho,
                    c.nacionalidade,
                    c.naturalidade,
                    c.data_nascimento,
                    c.tipo_documento,
                    c.numero_documento,
                    COALESCE(enc.id_entidade_certificadora, e.id_entidade) AS id_entidade,
                    COALESCE(ent_enc.designacao_comercial, e.designacao_comercial) AS designacao_comercial,
                    COALESCE(ent_enc.num_alvara, e.num_alvara) AS num_alvara,
                    q.id_qualificacao,
                    q.codigo_cnq,
                    q.denominacao AS denominacao_qualificacao,
                    q.nivel_qnq,
                    q.familia_profissional
                FROM public.rvcc_t_processo_rvcc p
                LEFT JOIN public.rvcc_t_candidato c
                    ON c.id_candidato = p.id_candidato
                LEFT JOIN public.rvcc_t_entidade e
                    ON e.id_entidade = p.id_entidade
                LEFT JOIN public.rvcc_t_ficha_percurso_profissional f
                    ON f.id_processo = p.id_processo
                LEFT JOIN public.rvcc_t_encaminhamento enc
                    ON enc.id_processo = p.id_processo
                LEFT JOIN public.rvcc_t_entidade ent_enc
                    ON ent_enc.id_entidade = enc.id_entidade_certificadora
                LEFT JOIN public.rvcc_t_qualificacao_profissional q
                    ON q.id_qualificacao = COALESCE(:idQualificacao, f.id_qualificacao, enc.id_qualificacao)
                WHERE p.id_processo = :idProcesso
                ORDER BY f.id_ficha DESC NULLS LAST, enc.id_encaminhamento DESC NULLS LAST
                LIMIT 1
                """, params, this::mapDadosCertificado);

        return dados.isEmpty() ? Optional.empty() : Optional.of(dados.get(0));
    }

    @Override
    public List<UnidadeCompetenciaReportDto> obterUnidadesCompetencia(Integer idProcesso, Integer idQualificacao) {
        if (idQualificacao == null) {
            return Collections.emptyList();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idProcesso", idProcesso)
                .addValue("idQualificacao", idQualificacao);

        return jdbcTemplate.query("""
                SELECT
                    uc.id_uc,
                    uc.codigo_uc,
                    uc.denominacao,
                    uc.carga_horaria,
                    uc.media_final,
                    uc.validada,
                    uc.data_avaliacao,
                    uc.resultado
                FROM (
                    SELECT DISTINCT ON (uc.codigo_uc)
                        uc.id_uc,
                        uc.codigo_uc,
                        uc.denominacao,
                        uc.carga_horaria,
                        uca.media_final,
                        uca.validada,
                        uca.data_avaliacao,
                        uca.resultado
                    FROM public.rvcc_t_unidade_competencia_avaliada uca
                    LEFT JOIN public.rvcc_t_avaliacao av
                        ON av.id_avaliacao = uca.id_avaliacao
                    JOIN public.rvcc_t_unidade_competencia uc
                        ON uc.id_uc = uca.id_uc
                    WHERE COALESCE(uca.id_processo, av.id_processo) = :idProcesso
                      AND uc.id_qualificacao = :idQualificacao
                      AND COALESCE(uca.validada, false) = true
                    ORDER BY uc.codigo_uc, uca.data_avaliacao DESC NULLS LAST, uca.id_uc_avaliada DESC
                ) uc
                ORDER BY uc.codigo_uc
                """, params, this::mapUnidadeCompetencia);
    }

    @Override
    public LocalDate obterDataAvaliacao(Integer idProcesso, Integer idQualificacao) {
        if (idQualificacao == null) {
            return null;
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("idProcesso", idProcesso)
                .addValue("idQualificacao", idQualificacao);

        return jdbcTemplate.query("""
                SELECT MAX(uca.data_avaliacao) AS data_avaliacao
                FROM public.rvcc_t_unidade_competencia_avaliada uca
                LEFT JOIN public.rvcc_t_avaliacao av
                    ON av.id_avaliacao = uca.id_avaliacao
                JOIN public.rvcc_t_unidade_competencia uc
                    ON uc.id_uc = uca.id_uc
                WHERE COALESCE(uca.id_processo, av.id_processo) = :idProcesso
                  AND uc.id_qualificacao = :idQualificacao
                  AND COALESCE(uca.validada, false) = true
                """, params, rs -> rs.next() ? rs.getObject("data_avaliacao", LocalDate.class) : null);
    }

    private DadosCertificado mapDadosCertificado(ResultSet rs, int rowNum) throws SQLException {
        String naturalidade = geografiaService.findNameById(rs.getString("naturalidade"));
        String concelho = geografiaService.findNameById(rs.getString("concelho"));
        String nacionalidade = geografiaService.findNameById(rs.getString("nacionalidade"));

        CandidatoReportDto candidato = new CandidatoReportDto(
                rs.getString("nome_completo"),
                naturalidadeRelatorio(naturalidade, concelho, nacionalidade),
                concelho,
                nacionalidade,
                format(rs.getObject("data_nascimento", LocalDate.class)),
                rs.getString("tipo_documento"),
                rs.getString("numero_documento"),
                documentoIdentificacao(rs.getString("tipo_documento"), rs.getString("numero_documento"))
        );

        EntidadeFormadoraReportDto entidade = new EntidadeFormadoraReportDto(
                rs.getString("designacao_comercial"),
                rs.getString("num_alvara"),
                null
        );

        QualificacaoReportDto qualificacao = new QualificacaoReportDto(
                getInteger(rs, "id_qualificacao"),
                rs.getString("codigo_cnq"),
                rs.getString("denominacao_qualificacao"),
                getInteger(rs, "nivel_qnq"),
                rs.getString("familia_profissional")
        );

        return new DadosCertificado(
                rs.getInt("id_processo"),
                rs.getString("num_processo"),
                rs.getObject("data_geracao_certificado", LocalDate.class),
                format(toLocalDate(rs.getTimestamp("data_conclusao"))),
                format(toLocalDate(rs.getTimestamp("data_submissao"))),
                candidato,
                entidade,
                qualificacao,
                getInteger(rs, "id_entidade")
        );
    }

    private UnidadeCompetenciaReportDto mapUnidadeCompetencia(ResultSet rs, int rowNum) throws SQLException {
        Integer cargaHoraria = getInteger(rs, "carga_horaria");
        return new UnidadeCompetenciaReportDto(
                rs.getInt("id_uc"),
                rs.getString("codigo_uc"),
                rs.getString("denominacao"),
                cargaHoraria,
                cargaHoraria == null ? null : cargaHoraria + " Horas",
                rs.getBigDecimal("media_final"),
                rs.getObject("validada", Boolean.class),
                format(rs.getObject("data_avaliacao", LocalDate.class)),
                rs.getString("resultado")
        );
    }

    private String naturalidadeRelatorio(String naturalidade, String concelho, String nacionalidade) {
        return primeiroNaoVazio(naturalidade, concelho, nacionalidade);
    }

    private String documentoIdentificacao(String tipoDocumento, String numeroDocumento) {
        if (!StringUtils.hasText(tipoDocumento)) {
            return numeroDocumento;
        }
        if (!StringUtils.hasText(numeroDocumento)) {
            return tipoDocumento;
        }
        return tipoDocumento + " no " + numeroDocumento;
    }

    private String primeiroNaoVazio(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }

    private LocalDate toLocalDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime().toLocalDate();
    }

    private String format(LocalDate value) {
        return value == null ? null : value.format(DATE_FORMAT);
    }

    private Integer getInteger(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }
}
