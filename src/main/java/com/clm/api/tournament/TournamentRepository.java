package com.clm.api.tournament;

import com.clm.api.enums.Visibility;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/** TounamentRepository */
@Repository
public interface TournamentRepository extends MongoRepository<Tournament, String> {

  Page<Tournament> findByVisibilityAndStartTimeAfter(
      Visibility visibility, Instant instant, Pageable pageable);

  Page<Tournament> findByVisibilityAndStartTimeBeforeAndEndTimeAfter(
      Visibility visibility,
      Instant startTimeMilestone,
      Instant endTimeMilestone,
      Pageable pageable);

  Page<Tournament> findByVisibilityAndEndTimeBefore(
      Visibility visibility, Instant instant, Pageable pageable);

  Page<Tournament> findByVisibility(Visibility visibility, Pageable pageable);

  Page<Tournament> findByVisibilityAndCancelled(
      Visibility visibility, Boolean cancelled, Pageable pageable);

  Page<Tournament> findByVisibilityAndNameContainingIgnoreCase(
      Visibility visibility, String name, Pageable pageable);
}
