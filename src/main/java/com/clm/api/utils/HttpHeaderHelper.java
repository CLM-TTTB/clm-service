package com.clm.api.utils;

import com.clm.api.exceptions.business.HttpHeaderMissingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

/** HttpHeaderValidator */
public class HttpHeaderHelper {

  /**
   * Get the value of the Authorization Bearer token from the HttpServletRequest.
   *
   * @param request The HttpServletRequest object.
   * @return The value of the Authorization Bearer token.
   */
  public static String getBearerToken(HttpServletRequest request)
      throws HttpHeaderMissingException {
    return getHeader(request, HttpHeaders.AUTHORIZATION, "Bearer ");
  }

  /**
   * Get the value of a specific header from the HttpServletRequest.
   *
   * @param request The HttpServletRequest object.
   * @param headerName The name of the header to retrieve.
   * @return The value of the specified header.
   */
  public static String getHeader(HttpServletRequest request, String headerName)
      throws HttpHeaderMissingException {
    String headerValue = request.getHeader(headerName);
    if (isNotValid(headerValue)) {
      throw new HttpHeaderMissingException(headerName);
    }
    return headerValue;
  }

  /**
   * Get the value of a header starting with a specific prefix.
   *
   * @param request The HttpServletRequest object.
   * @param headerName The name of the header to retrieve.
   * @param prefix The prefix of the header value.
   * @return The value of the header with the specified prefix.
   */
  public static String getHeader(HttpServletRequest request, String headerName, String prefix)
      throws HttpHeaderMissingException {
    String headerValue = request.getHeader(headerName);
    if (isNotValid(headerValue, prefix)) {
      throw new HttpHeaderMissingException(headerName, prefix.trim());
    }
    return headerValue.substring(prefix.length());
  }

  private static boolean isNotValid(String header) {
    return header == null || header.isEmpty();
  }

  private static boolean isNotValid(String header, String prefix) {
    return isNotValid(header) || !header.startsWith(prefix);
  }
}
