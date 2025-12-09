package com.ProdeMaster.MatchService.application.model;

import java.time.LocalDateTime;

public class LeagueModel {
    private Integer id;
    private Integer sportId;
    private Integer countryId;
    private String name;
    private Boolean active;
    private String shortCode;
    private String imagePath;
    private String type;
    private String subType;
    private LocalDateTime lastPlayedAt;
    private Integer category;
    private Boolean hasJerseys;

    public LeagueModel() {
    }

    public LeagueModel(Integer id, Integer sportId, Integer countryId, String name, Boolean active,
            String shortCode, String imagePath, String type, String subType,
            LocalDateTime lastPlayedAt, Integer category, Boolean hasJerseys) {
        this.id = id;
        this.sportId = sportId;
        this.countryId = countryId;
        this.name = name;
        this.active = active;
        this.shortCode = shortCode;
        this.imagePath = imagePath;
        this.type = type;
        this.subType = subType;
        this.lastPlayedAt = lastPlayedAt;
        this.category = category;
        this.hasJerseys = hasJerseys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public LocalDateTime getLastPlayedAt() {
        return lastPlayedAt;
    }

    public void setLastPlayedAt(LocalDateTime lastPlayedAt) {
        this.lastPlayedAt = lastPlayedAt;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Boolean getHasJerseys() {
        return hasJerseys;
    }

    public void setHasJerseys(Boolean hasJerseys) {
        this.hasJerseys = hasJerseys;
    }
}
