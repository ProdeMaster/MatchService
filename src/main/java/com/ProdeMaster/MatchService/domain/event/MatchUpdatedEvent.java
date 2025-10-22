package com.ProdeMaster.MatchService.domain.event;

import java.time.LocalDateTime;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;

public class MatchUpdatedEvent {
    private final Long matchId;
    private final String homeTeam;
    private final String awayTeam;
    private final Integer homeTeamScore;
    private final Integer awayTeamScore;
    private final MatchStatus status;
    private final LocalDateTime updateTime;
    private final String updateDescription;


    public MatchUpdatedEvent(Long matchId, String homeTeam, String awayTeam, Integer homeTeamScore, Integer awayTeamScore, MatchStatus status, LocalDateTime updateTime, String updateDescription) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.status = status;
        this.updateTime = updateTime;
        this.updateDescription = updateDescription;
    }

    // Getters
    public Long getMatchId() {
        return matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public String getUpdateDescription() {
        return updateDescription;
    }
}