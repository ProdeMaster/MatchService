package com.ProdeMaster.MatchService.infraestructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "status")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String name;

    public StatusEntity() {
    }

    public StatusEntity(String state, String name) {
        this.state = state;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
