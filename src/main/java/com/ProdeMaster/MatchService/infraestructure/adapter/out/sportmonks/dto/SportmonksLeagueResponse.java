package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksLeagueResponse {

    @JsonProperty("data")
    private List<LeagueData> leagues;

    public List<LeagueData> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<LeagueData> leagues) {
        this.leagues = leagues;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LeagueData {
        private Integer id;
        @JsonProperty("sport_id")
        private Integer sportId;
        @JsonProperty("country_id")
        private Integer countryId;
        private String name;
        private Boolean active;
        @JsonProperty("short_code")
        private String shortCode;
        @JsonProperty("image_path")
        private String imagePath;
        private String type;
        @JsonProperty("sub_type")
        private String subType;
        @JsonProperty("last_played_at")
        private String lastPlayedAt;
        private Integer category;
        @JsonProperty("has_jerseys")
        private Boolean hasJerseys;

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

        public String getLastPlayedAt() {
            return lastPlayedAt;
        }

        public void setLastPlayedAt(String lastPlayedAt) {
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
}
