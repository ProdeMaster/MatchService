package com.ProdeMaster.MatchService.application.model;

public class StateModel {
    private Integer id;
    private String state;
    private String name;
    private String shortName;
    private String developerName;

    public StateModel() {
    }

    public StateModel(Integer id, String state, String name, String shortName, String developerName) {
        this.id = id;
        this.state = state;
        this.name = name;
        this.shortName = shortName;
        this.developerName = developerName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public void setDeveloperName(String developerName) {
        this.developerName = developerName;
    }
}
