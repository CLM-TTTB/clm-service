package com.clm.api.searcher;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** SearchableController */
@RestController
@lombok.RequiredArgsConstructor
@RequestMapping("/v1/searcher")
public class SearchableController {

  public ResponseEntity<?> search(
      @RequestParam String q,
      @PageableDefault(page = 0, size = 9, sort = "createdAt", direction = Direction.ASC)
          Pageable pageable) {

    return null;
    // return ResponseEntity.ok(tournamentService.getAll(visibility, status, pageable));
  }
}
