package com.clm.api.upload;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import org.springframework.web.multipart.MultipartFile;

/** UploadService */
public interface FileService {

  String upload(MultipartFile file, Class<?> belongTo, Principal principal) throws IOException;

  byte[] get(String filePath, Principal principal) throws IOException;

  InputStream getStream(String filePath, Principal principal) throws IOException;

  boolean delete(String filePath, Principal principal) throws IOException;

  String update(String filePath, MultipartFile file, Principal principal) throws IOException;
}
