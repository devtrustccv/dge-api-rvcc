package cv.dge.dge_api_rvcc.common.exception;

import cv.dge.dge_api_rvcc.common.api.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(ImportacaoInvalidaException.class)
    public ResponseEntity<ApiErrorResponse> handleImportacaoInvalida(
            ImportacaoInvalidaException exception,
            HttpServletRequest request
    ) {
        log.warn("Erro de importacao na rota {}: {}", request.getRequestURI(), exception.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IntegracaoOrganizacaoException.class)
    public ResponseEntity<ApiErrorResponse> handleIntegracaoOrganizacao(
            IntegracaoOrganizacaoException exception,
            HttpServletRequest request
    ) {
        log.error("Erro de integracao com organizacao na rota {}: {}", request.getRequestURI(), exception.getMessage(), exception);
        return buildResponse(HttpStatus.BAD_GATEWAY, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccess(
            DataAccessException exception,
            HttpServletRequest request
    ) {
        log.error("Erro de base de dados na rota {}.", request.getRequestURI(), exception);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro de base de dados ao processar o pedido.",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleJsonInvalido(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        log.warn("JSON invalido na rota {}: {}", request.getRequestURI(), exception.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "JSON invalido ou mal formatado.", request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleErroGenerico(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error("Erro interno nao tratado na rota {}.", request.getRequestURI(), exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro interno.", request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        ));
    }
}
