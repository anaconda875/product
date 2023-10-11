package com.example.product.dto.response;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ErrorResponse {

  private LocalDateTime timestamp = LocalDateTime.now();
  private int status;
  private String error;
  private String path;

  private Map<String, List<FieldError>> fieldErrors = new LinkedHashMap<>();

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FieldError {

    private String message;
  }
}
