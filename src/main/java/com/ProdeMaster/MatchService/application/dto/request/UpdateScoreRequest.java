package com.ProdeMaster.MatchService.application.dto.request;

public class UpdateScoreRequest {

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    public UpdateScoreRequest() {
    }

    public UpdateScoreRequest(Integer homeTeamScore, Integer awayTeamScore) {
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
    }

    public Integer getHomeTeamScore() {
        return homeTeamScore;
    }

    public void setHomeTeamScore(Integer homeTeamScore) {
        this.homeTeamScore = homeTeamScore;
    }

    public Integer getAwayTeamScore() {
        return awayTeamScore;
    }

    public void setAwayTeamScore(Integer awayTeamScore) {
        this.awayTeamScore = awayTeamScore;
    }
}
