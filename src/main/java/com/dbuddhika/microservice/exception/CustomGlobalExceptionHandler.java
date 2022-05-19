package com.dbuddhika.microservice.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApiRequestException.class)
  public ResponseEntity<CustomErrorResponse> customHandleApiRequestException(Exception ex, WebRequest request) {

    return getCustomErrorResponseResponseEntity(ex, (ServletWebRequest) request);
  }

  @ExceptionHandler(OrderBookNotExistsException.class)
  public ResponseEntity<CustomErrorResponse> customHandleOrderBookNotExistsException(Exception ex, WebRequest request) {

    return getCustomErrorResponseResponseEntity(ex,
        (ServletWebRequest) request);
  }

  private ResponseEntity<CustomErrorResponse> getCustomErrorResponseResponseEntity(Exception ex,
      ServletWebRequest request) {
    CustomErrorResponse errors = new CustomErrorResponse();
    errors.setTimestamp(LocalDateTime.now());
    errors.setError(ex.getMessage());
    errors.setStatus(HttpStatus.BAD_REQUEST.value());
    errors.setPath(request.getRequest().getRequestURI().toString());

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }
}
