package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SpringDataMatchRepository extends JpaRepository<MatchEntity, Long> {

    // Find matches by league ID
    List<MatchEntity> findByLeagueId(Long leagueId);

    // Find matches by status ID
    List<MatchEntity> findByStatusId(Long statusId);

    // Find matches by date range
    @Query("SELECT m FROM MatchEntity m WHERE m.startingAt BETWEEN :startDate AND :endDate")
    List<MatchEntity> findByStartingAtBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find matches by name containing (for team search)
    @Query("SELECT m FROM MatchEntity m WHERE m.name LIKE %:teamName%")
    List<MatchEntity> findByNameContaining(@Param("teamName") String teamName);

    // Find by provider ID (SportMonks)
    List<MatchEntity> findByProviderId(Long providerId);

    // Exists by provider ID
    boolean existsByProviderId(Long providerId);

    // Find by provider ID + external match ID (unique combination)
    @Query("SELECT m FROM MatchEntity m WHERE m.providerId = :providerId AND m.externalMatchId = :externalMatchId")
    Optional<MatchEntity> findByProviderIdAndExternalMatchId(@Param("providerId") Long providerId,
            @Param("externalMatchId") Long externalMatchId);

    // Exists by provider ID + external match ID
    @Query("SELECT COUNT(m) > 0 FROM MatchEntity m WHERE m.providerId = :providerId AND m.externalMatchId = :externalMatchId")
    boolean existsByProviderIdAndExternalMatchId(@Param("providerId") Long providerId,
            @Param("externalMatchId") Long externalMatchId);
}
