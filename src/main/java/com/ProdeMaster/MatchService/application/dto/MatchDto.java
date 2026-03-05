package com.ProdeMaster.MatchService.application.dto;

public class MatchDto {

    private Long id;
    private String homeTeam;
    private String awayTeam;
    private String league;
    private String matchDateTime;
    private String status;

    public MatchDto(Long id, String homeTeam, String awayTeam, String league, String matchDateTime, String status) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public String getHomeTeam() {
        return this.homeTeam;
    }

    public String getAwayTeam() {
        return this.awayTeam;
    }

    public String getLeague() {
        return this.league;
    }

    public String getMatchDateTime() {
        return this.matchDateTime;
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "Match id: " + this.id + "\n" + "League: " + this.league + "\n" + "Local Team= '" + this.homeTeam
                + "', Away Team= '" + this.awayTeam + "', " + "status= '" + this.status + "'" + "\n";
    }
}