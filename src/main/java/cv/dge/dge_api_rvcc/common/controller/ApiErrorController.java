package cv.dge.dge_api_rvcc.common.controller;

import cv.dge.dge_api_rvcc.common.api.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
public class ApiErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public ApiErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public ResponseEntity<ApiErrorResponse> error(HttpServletRequest request) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                new ServletWebRequest(request),
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)
        );

        int status = (int) attributes.getOrDefault("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        String error = Objects.toString(attributes.getOrDefault("error", "Unexpected Error"));
        String message = Objects.toString(attributes.getOrDefault("message", "Ocorreu um erro inesperado."));
        String path = Objects.toString(attributes.getOrDefault("path", request.getRequestURI()));

        return ResponseEntity.status(HttpStatusCode.valueOf(status)).body(new ApiErrorResponse(
                OffsetDateTime.now(),
                status,
                error,
                message,
                path
        ));
    }
}