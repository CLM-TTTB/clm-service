package com.clm.api.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.apache.commons.lang3.function.TriFunction;

/** HttpUriHelper */
public class HttpUriHelper {

  /**
   * Convert string with format "name:searchValue,address:searchValue" to map
   *
   * @param query the query
   * @return the map with key value pair with format {name:searchValue,address:searchValue}
   */
  public static final Map<String, Object> convertStringToMap(String query) {
    String[] params = query.split(",");
    Map<String, Object> map = new HashMap<>();
    Arrays.stream(params)
        .forEach(
            (param) -> {
              String[] p = param.trim().split(":");
              if (p.length == 2) {
                map.put(p[0].trim(), p[1].trim());
              }
            });
    return map;
  }

  public static final <T> Map<String, T> convertStringToMap(String query, Class<T> type) {
    String[] params = query.split(",");
    Map<String, T> map = new HashMap<>();
    Arrays.stream(params)
        .forEach(
            (param) -> {
              String[] p = param.trim().split(":");
              if (p.length == 2) {
                map.put(p[0].trim(), convertStringToType(p[1].trim(), type));
              }
            });
    return map;
  }

  @SuppressWarnings("unchecked")
  private static <T> T convertStringToType(String value, Class<T> targetType) {
    if (targetType == String.class) {
      return (T) value;
    } else if (targetType == Integer.class) {
      return (T) Integer.valueOf(value);
    } else if (targetType == Long.class) {
      return (T) Long.valueOf(value);
    } else if (targetType == Double.class) {
      return (T) Double.valueOf(value);
    } else if (targetType == Float.class) {
      return (T) Float.valueOf(value);
    } else if (targetType == Boolean.class) {
      return (T) Boolean.valueOf(value);
    } else if (targetType == Byte.class) {
      return (T) Byte.valueOf(value);
    } else if (targetType == Short.class) {
      return (T) Short.valueOf(value);
    } else if (targetType == Character.class) {
      return (T) Character.valueOf(value.charAt(0));
    } else {
      return null;
    }
  }

  /**
   * Convert list of string with format ["name:searchValue","address:searchValue"] to map
   *
   * @param query the query
   * @return the map with key value pair with format {name:searchValue,address:searchValue}
   */
  public static final Map<String, Object> converListStringToMap(List<String> query) {
    Map<String, Object> map = new HashMap<String, Object>();
    query.stream()
        .forEach(
            (param) -> {
              String[] p = param.trim().split(":");
              if (p.length == 2) {
                map.put(p[0], p[1]);
              }
            });
    return map;
  }

  /**
   * Get items with pagination
   *
   * @param <T> the type parameter
   * @param page the page
   * @param limit the limit
   * @param all the all
   * @param func the func with params skip and limit and return list of items with pagination
   * @return the items with pagination
   */
  public static final <T> List<T> getItemsWithPagination(
      BiFunction<Integer, Integer, List<T>> func, int page, int limit, boolean all) {
    int skip = all ? 0 : (page - 1) * limit;
    return func.apply(skip, all ? (page * limit) : limit);
  }

  /**
   * Get items with pagination
   *
   * @param <T> the type parameter
   * @param page the page
   * @param limit the limit
   * @param all the all
   * @param func the func with params skip and limit and return list of items with pagination
   * @return the items with pagination
   */
  public static final <T> List<T> getItemsWithPagination(
      TriFunction<Integer, Integer, Object[], List<T>> func,
      int page,
      int limit,
      boolean all,
      Object[]... objects) {
    int skip = all ? 0 : (page - 1) * limit;
    return func.apply(skip, all ? (page * limit) : limit, objects);
  }
}
