package com.ProdeMaster.MatchService.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("Match")
public class MatchModel implements Serializable {
    @Id
    private String id;

    private String team1; //Home Team
    private String team2; //Away Team

    private String league;
    private LocalDateTime matchDateTime;

    private String status; // Ejemplo: "scheduled", "in_progress", "finished"
    private Integer scoreTeam1;
    private Integer scoreTeam2;

    private boolean resultConfirmed;

    public MatchModel() {
    }

    public MatchModel(String id, String team1, String team2, String league, LocalDateTime matchDateTime, String status, boolean resultConfirmed) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = status;
        this.resultConfirmed = resultConfirmed;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getLeague() {
        return league;
    }

    public LocalDateTime getMatchDateTime() {
        return matchDateTime;
    }

    public String getStatus() {
        return status;
    }

    public Integer getScoreTeam1() {
        return scoreTeam1;
    }

    public Integer getScoreTeam2() {
        return scoreTeam2;
    }

    public boolean isResultConfirmed() {
        return resultConfirmed;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setMatchDateTime(LocalDateTime matchDateTime) {
        this.matchDateTime = matchDateTime;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScoreTeam1(Integer scoreTeam1) {
        this.scoreTeam1 = scoreTeam1;
    }

    public void setScoreTeam2(Integer scoreTeam2) {
        this.scoreTeam2 = scoreTeam2;
    }

    public void setResultConfirmed(boolean resultConfirmed) {
        this.resultConfirmed = resultConfirmed;
    }
}
