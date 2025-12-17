package com.ProdeMaster.MatchService.infraestructure.entity;

import jakarta.persistence.*;
import org.apache.kafka.common.protocol.types.Field;

@Entity
@Table (name = "states")
public class StateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String name;

    public StateEntity() {
    }

    public StateEntity(String state, String name) {
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
