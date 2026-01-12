package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksTeamResponse {

    @JsonProperty("data")
    private List<TeamData> teams;

    public List<TeamData> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamData> teams) {
        this.teams = teams;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamData {
        private Integer id;
        @JsonProperty("sport_id")
        private Integer sportId;
        @JsonProperty("country_id")
        private Integer countryId;
        @JsonProperty("venue_id")
        private Integer venueId;
        private String gender;
        private String name;
        @JsonProperty("short_code")
        private String shortCode;
        @JsonProperty("image_path")
        private String imagePath;
        private Integer founded;
        private String type;
        private Boolean placeholder;
        @JsonProperty("last_played_at")
        private String lastPlayedAt;

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

        public Integer getVenueId() {
            return venueId;
        }

        public void setVenueId(Integer venueId) {
            this.venueId = venueId;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public Integer getFounded() {
            return founded;
        }

        public void setFounded(Integer founded) {
            this.founded = founded;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(Boolean placeholder) {
            this.placeholder = placeholder;
        }

        public String getLastPlayedAt() {
            return lastPlayedAt;
        }

        public void setLastPlayedAt(String lastPlayedAt) {
            this.lastPlayedAt = lastPlayedAt;
        }
    }
}
