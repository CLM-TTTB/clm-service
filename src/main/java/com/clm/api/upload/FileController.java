package com.clm.api.upload;

import com.clm.api.tournament.Tournament;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** UploadController */
@RestController
@RequestMapping("/v1/files/")
@lombok.RequiredArgsConstructor
public class FileController {

  // public static final String UPLOAD_PATH = "/upload";
  //
  private final FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Principal connectedUser)
      throws IOException {
    try {
      String savedPath = fileService.upload(file, Tournament.class, connectedUser);
      return ResponseEntity.ok(savedPath);
    } catch (IOException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/{filename}")
  public void download(
      @PathVariable("filename") String filename,
      HttpServletRequest request,
      HttpServletResponse response,
      Principal principal) {
    try {
      InputStream inputStream =
          fileService.getStream(
              "client_upload/images/659bc4055673d8069927f329/Tournament/651b5673-c9dc-406a-b8a9-cafc7d204954_2024-01-09T06:12:50.896139843Z.jpg",
              principal);

      response.setContentType(MediaType.IMAGE_JPEG_VALUE);
      response.setHeader("Content-Disposition", "attachment; filename=" + filename);
      StreamUtils.copy(inputStream, response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
