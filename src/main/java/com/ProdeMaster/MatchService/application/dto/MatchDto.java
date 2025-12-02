package com.ProdeMaster.MatchService.application.dto;

public class MatchDto {

    private Long id;
    private String team1;
    private String team2;
    private String league;
    private String matchDateTime;
    private String status; // Ejemplo: "scheduled", "in_progress", "finished"

    public MatchDto(Long id, String team1, String team2, String league, String matchDateTime, String status) {
        this.id = id;
        this.team1 = team1;
        this.team2 = team2;
        this.league = league;
        this.matchDateTime = matchDateTime;
        this.status = status;
    }

    public Long getId() { return id;}
    public String getTeam1() { return team1; }
    public String getTeam2() { return team2; }
    public String getLeague() { return league; }
    public String getMatchDateTime() { return matchDateTime; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "Match id: " + id + "\n" + "League: " + league + "\n" + "Local Team= '" + team1 + "', Visiting Team= '" + team2 + "', " + "status= '" + status +"'" + "\n";
    }
}

// return "MatchDto{id=" + id + ", local-team='" + team1 + "', visiting-team='" + team2 + "', " + "status='" + status +"'}";