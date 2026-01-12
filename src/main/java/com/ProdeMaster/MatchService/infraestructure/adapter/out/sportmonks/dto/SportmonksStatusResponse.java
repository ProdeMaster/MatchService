package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksStatusResponse {

    @JsonProperty("state")
    private List<StatusData> states;

    public List<StatusData> getStates() {
        return states;
    }

    public void setStates(List<StatusData> states) {
        this.states = states;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StatusData {
        private Integer id;
        private String state;
        private String name;
        @JsonProperty("short_name")
        private String shortName;
        @JsonProperty("developer_name")
        private String developerName;

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
}
