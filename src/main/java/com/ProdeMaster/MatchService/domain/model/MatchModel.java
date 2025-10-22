package com.ProdeMaster.MatchService.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class MatchModel implements Serializable {
    private Long matchId;

    private String homeTeam;
    private String awayTeam;
    private String league;
    private LocalDateTime matchDateTime;
    private MatchStatus status;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Boolean resultConfirmed;

    public MatchModel() {}

    public MatchModel(Long matchId, String team1, String team2, String league, LocalDateTime matchDateTime, MatchStatus status, Boolean resultConfirmed) {
        this.matchId = matchId;
        this.homeTeam = team1;
        this.awayTeam = team2;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = status;
        this.resultConfirmed = resultConfirmed;
    }

    public Long getMatchId() { return matchId; }

    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getLeague() {
        return league;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public Boolean isResultConfirmed() {
        return resultConfirmed;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setMatchDateTime(LocalDateTime matchDateTime) {
        this.matchDateTime = matchDateTime;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public void setResultConfirmed(Boolean resultConfirmed) {
        this.resultConfirmed = resultConfirmed;
    }
}
