package com.example.product.exception.handler;

import com.example.product.dto.response.ErrorResponse;
import java.util.LinkedList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  //  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    errorResponse.setError(ex.getMessage());
    errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());

    // 3xx > redirection
    // 4xx > client error (bad request, v.v)
    // 5xx > server error (bad gateway, service un-avalable, v.v)
    return ResponseEntity.internalServerError().body(errorResponse);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setPath(((ServletWebRequest) request).getRequest().getRequestURI());
    errorResponse.setError(ex.getClass().toString());
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

    List<FieldError> fieldErrors = ex.getFieldErrors();
    addFieldErrorsToResponse(errorResponse, fieldErrors);

    return ResponseEntity.badRequest().body(errorResponse);
  }

  private void addFieldErrorsToResponse(ErrorResponse errorResponse, List<FieldError> fieldErrors) {
    fieldErrors.forEach(
        fe -> {
          List<ErrorResponse.FieldError> customFieldErrors =
              errorResponse
                  .getFieldErrors()
                  .computeIfAbsent(fe.getField(), k -> new LinkedList<>());
          customFieldErrors.add(new ErrorResponse.FieldError(fe.getDefaultMessage()));
        });
  }
}
