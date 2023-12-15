package com.clm.api.constants;

/** WhiteListUrl */
public class WhiteListUrl {
  public static final String[] GET_METHODS = {
    "/v1/tournaments/**",
  };

  public static final String[] POST_METHODS = {};

  public static final String[] PUT_METHODS = {};

  public static final String[] DELETE_METHODS = {};

  public static final String[] PATCH_METHODS = {
    // "/v1/admin/**",
  };

  public static final String[] ALL_METHODS = {
    "/v1/public/**",
    "/v1/auth/**",
    "/v1/test/**",
    "/configuration/ui",
    "/configuration/security",
    "/webjars/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/swagger-resources",
    "/swagger-resources/**",
    "/v2/api-docs",
    "/v3/api-docs",
    "/v3/api-docs/**",
    "/favicon.ico",
  };
}
