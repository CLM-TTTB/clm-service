package com.clm.api.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.springframework.http.HttpStatus;

/** ErrorMessage */
@lombok.Data
@lombok.AllArgsConstructor
@lombok.Builder
@lombok.Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
  private String error;
  private String message;
  private Date timestamp;
  private HttpStatus status;
  private int code;
  private String path;
  private Object data;
}
