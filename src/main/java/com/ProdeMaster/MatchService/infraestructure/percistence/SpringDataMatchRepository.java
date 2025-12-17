package com.ProdeMaster.MatchService.infraestructure.percistence;

import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataMatchRepository extends JpaRepository<MatchEntity, Long> {

    // Find matches by league ID
    List<MatchEntity> findByLeagueId(Integer leagueId);

    // Find matches by state ID
    List<MatchEntity> findByStateId(Integer stateId);

    // Find matches by date range
    @Query("SELECT m FROM MatchEntity m WHERE m.startingAt BETWEEN :startDate AND :endDate")
    List<MatchEntity> findByStartingAtBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find matches by name containing (for team search)
    @Query("SELECT m FROM MatchEntity m WHERE m.name LIKE %:teamName%")
    List<MatchEntity> findByNameContaining(@Param("teamName") String teamName);
}
