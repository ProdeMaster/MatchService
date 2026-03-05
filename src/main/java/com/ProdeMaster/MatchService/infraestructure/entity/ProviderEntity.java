package com.ProdeMaster.MatchService.infraestructure.entity;

import jakarta.persistence.*;

@Entity
@Table (name = "providers")
public class ProviderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public ProviderEntity() {
    }

    public ProviderEntity(String name) {
        this.name = name;
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


}
