package com.clm.api.utils;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** ObjectFieldComparator */
public class ObjectFieldComparator {

  public static <T> boolean existsWithField(
      List<T> list, T object, Function<T, Object> getFieldCallback) {
    if (list == null) return false;
    return list.stream()
        .anyMatch(o -> getFieldCallback.apply(o).equals(getFieldCallback.apply(object)));
  }

  public static <T1, T2> boolean existsWithField(
      List<T1> list,
      T2 object,
      Function<T1, Object> getFieldListItemCallback,
      Function<T2, Object> getFieldObjectCallback2) {
    if (list == null) return false;
    return list.stream()
        .anyMatch(
            o -> getFieldListItemCallback.apply(o).equals(getFieldObjectCallback2.apply(object)));
  }

  public static <T> boolean exists(List<T> list, Predicate<T> arg0) {
    if (list == null) return false;
    return list.stream().anyMatch(arg0);
  }
}
