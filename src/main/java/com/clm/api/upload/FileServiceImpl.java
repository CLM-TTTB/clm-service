package com.clm.api.upload;

import com.clm.api.exceptions.business.NotFoundException;
import com.clm.api.user.User;
import com.clm.api.utils.FileHelper;
import com.clm.api.utils.PrincipalHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** FileServiceImpl */
@Service
@lombok.RequiredArgsConstructor
public class FileServiceImpl implements FileService {

  @Value("${clm.upload.path.image}")
  private String path;

  @Override
  public String upload(MultipartFile file, Class<?> belongTo, Principal principal)
      throws IOException {
    User user = PrincipalHelper.getUser(principal);

    String savedDir = Paths.get(path, user.getId(), belongTo.getSimpleName()).toString();
    String savedName = FileHelper.randomNewName(file.getOriginalFilename());
    String savedPath = Paths.get(savedDir, savedName).toString();

    File fileToSave = new File(savedPath);

    if (!fileToSave.getParentFile().exists()) {
      fileToSave.getParentFile().mkdirs();
    }

    Files.copy(file.getInputStream(), fileToSave.toPath());
    return savedPath;
  }

  @Override
  public byte[] get(String filePath, Principal principal) throws IOException {
    Path path = Paths.get(filePath);

    if (Files.exists(path)) {
      return Files.readAllBytes(path);
    }
    throw new NotFoundException("File not found");
  }

  @Override
  public boolean delete(String filePath, Principal principal) throws IOException {
    return Files.deleteIfExists(Paths.get(filePath));
  }

  @Override
  public String update(String filePath, MultipartFile file, Principal principal)
      throws IOException {
    String savedDir = Paths.get(filePath).getParent().toString();
    String savedName = FileHelper.randomNewName(filePath);
    String savedPath = Paths.get(savedDir, savedName).toString();

    if (delete(filePath, principal)) {
      File fileToSave = new File(savedPath);

      if (!fileToSave.getParentFile().exists()) {
        fileToSave.getParentFile().mkdirs();
      }
      Files.copy(file.getInputStream(), fileToSave.toPath());
      return savedPath;
    }
    throw new NotFoundException("File not found");
  }

  @Override
  public InputStream getStream(String filePath, Principal principal) throws IOException {
    InputStream inputStream = Files.newInputStream(Paths.get(filePath));

    if (inputStream != null) {
      return inputStream;
    }

    throw new NotFoundException("File not found");
  }
}
