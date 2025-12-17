package com.ProdeMaster.MatchService.infraestructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class TeamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column (name = "league_id")
    private Long leagueId;

    public TeamEntity() {
    }

    public TeamEntity(String name, Long leagueId) {
        this.name = name;
        this.leagueId = leagueId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }
}
