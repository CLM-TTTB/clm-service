package com.clm.api.utils;

import java.time.Instant;
import java.util.UUID;

/** FileHelper */
public class FileHelper {
  public static final String getExtension(final String filename) {
    if (filename == null) return null;
    final String afterLastSlash = filename.substring(filename.lastIndexOf('/') + 1);
    final int afterLastBackslash = afterLastSlash.lastIndexOf('\\') + 1;
    final int dotIndex = afterLastSlash.indexOf('.', afterLastBackslash);
    return (dotIndex == -1) ? "" : afterLastSlash.substring(dotIndex + 1);
  }

  public static final String randomNewName(final String oldName) {
    String extension = getExtension(oldName);
    return UUID.randomUUID().toString() + "_" + Instant.now().toString() + "." + extension;
  }

  public static final String concat(final String sep, final String... paths) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < paths.length; i++) {
      sb.append(paths[i]);
      if (i < paths.length - 1
          && paths[i] != null
          && !paths[i].isBlank()
          && paths[i + 1] != null
          && !paths[i + 1].isBlank()) sb.append(sep);
    }
    return sb.toString();
  }
}
