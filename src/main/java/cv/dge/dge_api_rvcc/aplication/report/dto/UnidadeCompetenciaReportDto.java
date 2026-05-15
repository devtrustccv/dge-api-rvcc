package cv.dge.dge_api_rvcc.aplication.report.dto;

import java.math.BigDecimal;

public record UnidadeCompetenciaReportDto(
        Integer idUc,
        String codigo,
        String nome,
        Integer cargaHoraria,
        String duracaoFormacao,
        BigDecimal mediaFinal,
        Boolean validada,
        String dataAvaliacao,
        String resultado
) {
}
