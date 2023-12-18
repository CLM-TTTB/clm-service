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
      Visibility visibility, Instant now, Pageable pageable);

  Page<Tournament> findByVisibilityAndStartTimeBeforeAndEndTimeAfter(
      Visibility visibility,
      Instant startTimeMilestone,
      Instant endTimeMilestone,
      Pageable pageable);

  Page<Tournament> findByVisibilityAndEndTimeBefore(
      Visibility visibility, Instant now, Pageable pageable);

  Page<Tournament> findByVisibility(Visibility visibility, Pageable pageable);

  Page<Tournament> findByVisibilityAndCancelled(
      Visibility visibility, Boolean cancelled, Pageable pageable);

  Page<Tournament> findByVisibilityAndNameContainingIgnoreCase(
      Visibility visibility, String name, Pageable pageable);

  Page<Tournament> findByCreatorIdAndVisibility(
      String creatorId, Visibility visibility, Pageable pageable);

  Page<Tournament> findByCreatorId(String creatorId, Pageable pageable);

  Page<Tournament> findByCreatorIdAndVisibilityAndCancelled(
      String creatorId, Visibility visibility, Boolean cancelled, Pageable pageable);

  Page<Tournament> findByCreatorIdAndVisibilityAndStartTimeBeforeAndEndTimeAfter(
      String creatorId,
      Visibility visibility,
      Instant startTimeMilestone,
      Instant endTimeMilestone,
      Pageable pageable);

  Page<Tournament> findByCreatorIdAndVisibilityAndEndTimeBefore(
      String creatorId, Visibility visibility, Instant now, Pageable pageable);

  Page<Tournament> findByCreatorIdAndVisibilityAndStartTimeAfter(
      String creatorId, Visibility visibility, Instant now, Pageable pageable);

  Page<Tournament> findByCreatorIdAndStartTimeBeforeAndEndTimeAfter(
      String id, Instant now, Instant now2, Pageable pageable);

  Page<Tournament> findByCreatorIdAndCancelled(String id, boolean b, Pageable pageable);

  Page<Tournament> findByCreatorIdAndStartTimeAfter(String id, Instant now, Pageable pageable);

  Page<Tournament> findByCreatorIdAndEndTimeBefore(String id, Instant now, Pageable pageable);
}
