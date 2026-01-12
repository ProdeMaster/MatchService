package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksFixtureResponse {

    @JsonProperty("data")
    private List<FixtureData> fixtures;

    public List<FixtureData> getFixtures() {
        return fixtures;
    }

    public void setFixtures(List<FixtureData> fixtures) {
        this.fixtures = fixtures;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FixtureData {
        private Long id;
        @JsonProperty("sport_id")
        private Integer sportId;
        @JsonProperty("league_id")
        private Integer leagueId;
        @JsonProperty("season_id")
        private Integer seasonId;
        @JsonProperty("stage_id")
        private Integer stageId;
        @JsonProperty("group_id")
        private Integer groupId;
        @JsonProperty("aggregate_id")
        private Integer aggregateId;
        @JsonProperty("round_id")
        private Integer roundId;
        @JsonProperty("state_id")
        private Integer stateId;
        @JsonProperty("venue_id")
        private Integer venueId;
        private String name;
        @JsonProperty("starting_at")
        private String startingAt;
        @JsonProperty("result_info")
        private String resultInfo;
        private String leg;
        private String details;
        private Integer length;
        private Boolean placeholder;
        @JsonProperty("has_odds")
        private Boolean hasOdds;
        @JsonProperty("has_premium_odds")
        private Boolean hasPremiumOdds;
        @JsonProperty("starting_at_timestamp")
        private Long startingAtTimestamp;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Integer getSportId() {
            return sportId;
        }

        public void setSportId(Integer sportId) {
            this.sportId = sportId;
        }

        public Integer getLeagueId() {
            return leagueId;
        }

        public void setLeagueId(Integer leagueId) {
            this.leagueId = leagueId;
        }

        public Integer getSeasonId() {
            return seasonId;
        }

        public void setSeasonId(Integer seasonId) {
            this.seasonId = seasonId;
        }

        public Integer getStageId() {
            return stageId;
        }

        public void setStageId(Integer stageId) {
            this.stageId = stageId;
        }

        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public Integer getAggregateId() {
            return aggregateId;
        }

        public void setAggregateId(Integer aggregateId) {
            this.aggregateId = aggregateId;
        }

        public Integer getRoundId() {
            return roundId;
        }

        public void setRoundId(Integer roundId) {
            this.roundId = roundId;
        }

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public Integer getVenueId() {
            return venueId;
        }

        public void setVenueId(Integer venueId) {
            this.venueId = venueId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStartingAt() {
            return startingAt;
        }

        public void setStartingAt(String startingAt) {
            this.startingAt = startingAt;
        }

        public String getResultInfo() {
            return resultInfo;
        }

        public void setResultInfo(String resultInfo) {
            this.resultInfo = resultInfo;
        }

        public String getLeg() {
            return leg;
        }

        public void setLeg(String leg) {
            this.leg = leg;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public Integer getLength() {
            return length;
        }

        public void setLength(Integer length) {
            this.length = length;
        }

        public Boolean getPlaceholder() {
            return placeholder;
        }

        public void setPlaceholder(Boolean placeholder) {
            this.placeholder = placeholder;
        }

        public Boolean getHasOdds() {
            return hasOdds;
        }

        public void setHasOdds(Boolean hasOdds) {
            this.hasOdds = hasOdds;
        }

        public Boolean getHasPremiumOdds() {
            return hasPremiumOdds;
        }

        public void setHasPremiumOdds(Boolean hasPremiumOdds) {
            this.hasPremiumOdds = hasPremiumOdds;
        }

        public Long getStartingAtTimestamp() {
            return startingAtTimestamp;
        }

        public void setStartingAtTimestamp(Long startingAtTimestamp) {
            this.startingAtTimestamp = startingAtTimestamp;
        }
    }
}
