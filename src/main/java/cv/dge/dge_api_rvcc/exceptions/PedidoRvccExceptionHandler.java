package cv.dge.dge_api_rvcc.exceptions;

import cv.dge.dge_api_rvcc.common.api.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PedidoRvccExceptionHandler {

    @ExceptionHandler(PedidoRvccInvalidoException.class)
    public ResponseEntity<ApiErrorResponse> handlePedidoRvccInvalido(
            PedidoRvccInvalidoException exception,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorResponse(
                OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        ));
    }
}
