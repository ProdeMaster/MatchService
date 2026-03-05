package com.ProdeMaster.MatchService.application.dto.response;

import com.ProdeMaster.MatchService.domain.model.MatchStatus;

import java.time.LocalDateTime;

public class MatchResponse {

    private Long matchId;
    private String homeTeam;
    private String awayTeam;
    private String league;
    private LocalDateTime matchDateTime;
    private MatchStatus status;
    private Integer homeTeamScore;
    private Integer awayTeamScore;
    private Boolean resultConfirmed;

    public MatchResponse() {
    }

    public MatchResponse(Long matchId, String homeTeam, String awayTeam, String league,
                         LocalDateTime matchDateTime, MatchStatus status,
                         Integer homeTeamScore, Integer awayTeamScore, Boolean resultConfirmed) {
        this.matchId = matchId;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = status;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.resultConfirmed = resultConfirmed;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public void setMatchDateTime(LocalDateTime matchDateTime) {
        this.matchDateTime = matchDateTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }

    public Boolean getResultConfirmed() {
        return resultConfirmed;
    }

    public void setResultConfirmed(Boolean resultConfirmed) {
        this.resultConfirmed = resultConfirmed;
    }
}
