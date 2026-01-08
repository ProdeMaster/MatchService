package com.ProdeMaster.MatchService.application.dto;

public class TeamDto {
    private Long id;
    private String name;
    private Long leagueId;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public TeamDto(Long id, String name, Long leagueId) {
        this.id = id;
        this.name = name;
        this.leagueId = leagueId;
    }

    @Override
    public String toString() {
        return "TeamDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", leagueId=" + leagueId +
                '}';
    }
}