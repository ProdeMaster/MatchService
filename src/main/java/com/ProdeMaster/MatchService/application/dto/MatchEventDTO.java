package com.ProdeMaster.MatchService.application.dto;

public class MatchEventDTO {
    private Long matchId;
    private Long homeTeamId;
    private Long awayTeamId;
    private String matchDateTime;
    private String league;

    public MatchEventDTO(Long matchId, Long homeTeamId, Long awayTeamId, String matchDateTime, String league) {
        this.matchId = matchId;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchDateTime = matchDateTime;
        this.league = league;
    }

    public Long getMatchId() { return matchId; }
    public Long getHomeTeamId() { return homeTeamId; }
    public Long getAwayTeamId() { return awayTeamId; }
    public String getDate() { return matchDateTime; }
    public String getLeague() { return league; }
}
