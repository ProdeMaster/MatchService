package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksApiResponse {

    @JsonProperty("data")
    private List<SportmonksFixtureResponse.FixtureData> fixtures;

    private String message;

    public List<SportmonksFixtureResponse.FixtureData> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<SportmonksFixtureResponse.FixtureData> fixtures) {
        this.fixtures = fixtures;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
