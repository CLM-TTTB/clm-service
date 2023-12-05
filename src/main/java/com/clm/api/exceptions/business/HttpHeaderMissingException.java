package com.clm.api.exceptions.business;

import org.springframework.http.HttpStatus;

@lombok.Getter
public class HttpHeaderMissingException extends BusinessException {

  private String headerName;
  private String headerValue;

  public HttpHeaderMissingException(String headerName) {
    super(HttpStatus.BAD_REQUEST, "Header " + headerName + " is missing");
    this.headerName = headerName;
    this.headerValue = null;
  }

  public HttpHeaderMissingException(String headerName, String headerValue) {
    this(HttpStatus.BAD_REQUEST, headerName, headerValue);
  }

  public HttpHeaderMissingException(HttpStatus status, String headerName, String headerValue) {
    super(status, "Header " + headerName + " with value " + headerValue + " is missing");
    this.headerName = headerName;
    this.headerValue = headerValue;
  }

  public HttpHeaderMissingException(String headerName, String headerValue, Throwable cause) {
    this(HttpStatus.BAD_REQUEST, headerName, headerValue, cause);
  }

  public HttpHeaderMissingException(
      HttpStatus status, String headerName, String headerValue, Throwable cause) {
    super(status, "Header " + headerName + " with value " + headerValue + " is missing", cause);
    this.headerName = headerName;
    this.headerValue = headerValue;
  }
}
