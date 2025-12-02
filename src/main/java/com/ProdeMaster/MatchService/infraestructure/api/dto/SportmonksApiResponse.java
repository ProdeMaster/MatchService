package com.ProdeMaster.MatchService.infraestructure.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SportmonksApiResponse {
    @JsonProperty("data")
    private List<ApiResponse> data;
    private String message;

        public static class ApiResponse {
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
            private League league;
            private State state;

            public static class League {
                private Integer id;
                @JsonProperty("sport_id")
                private Integer sportId;
                @JsonProperty("country_id")
                private int countryId;
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

                public Integer getId() { return id; }
                public void setId(Integer id) { this.id = id; }

                public Integer getSportId() { return sportId; }
                public void setSportId(Integer sportId) { this.sportId = sportId; }

                public int getCountryId() { return countryId; }
                public void setCountryId(int countryId) { this.countryId = countryId; }

                public String getName() { return name; }
                public void setName(String name) { this.name = name; }

                public Boolean isActive() { return active; }
                public void setActive(Boolean active) { this.active = active; }

                public String getShortCode() { return shortCode; }
                public void setShortCode(String shortCode) { this.shortCode = shortCode; }

                public String getImagePath() { return imagePath; }
                public void setImagePath(String imagePath) { this.imagePath = imagePath; }

                public String getType() { return type; }
                public void setType(String type) { this.type = type; }

                public String getSubType() { return subType; }
                public void setSubType(String subType) { this.subType = subType; }

                public String getLastPlayedAt() { return lastPlayedAt; }
                public void setLastPlayedAt(String lastPlayedAt) { this.lastPlayedAt = lastPlayedAt; }

                public Integer getCategory() { return category; }
                public void setCategory(Integer category) { this.category = category; }

                public Boolean isHasJerseys() { return hasJerseys; }
                public void setHasJerseys(Boolean hasJerseys) { this.hasJerseys = hasJerseys; }
            }

            public static class State {
                private Integer id;
                private String state;
                private String name;
                @JsonProperty("short_name")
                private String shortName;
                private String developer_name;

                public Integer getId() { return id; }
                public void setId(Integer id) { this.id = id; }

                public String getState() { return state; }
                public void setState(String state) { this.state = state; }

                public String getName() { return name; }
                public void setName(String name) { this.name = name; }

                public String getShortName() { return shortName; }
                public void setShortName(String shortName) { this.shortName = shortName; }

                public String getDeveloper_name() { return developer_name; }
                public void setDeveloper_name(String developer_name) { this.developer_name = developer_name; }
            }

            public Long getId() { return id; }
            public void setId(Long id) { this.id = id; }

            public Integer getSportId() { return sportId; }
            public void setSportId(Integer sportId) { this.sportId = sportId; }

            public Integer getLeagueId() { return leagueId; }
            public void setLeagueId(Integer leagueId) { this.leagueId = leagueId; }

            public Integer getSeasonId() { return seasonId; }
            public void setSeasonId(Integer seasonId) { this.seasonId = seasonId; }

            public Integer getStageId() { return stageId; }
            public void setStageId(Integer stageId) { this.stageId = stageId; }

            public Integer getGroupId() { return groupId; }
            public void setGroupId(Integer groupId) { this.groupId = groupId; }

            public Integer getAggregateId() { return aggregateId; }
            public void setAggregateId(Integer aggregateId) { this.aggregateId = aggregateId; }

            public Integer getRoundId() { return roundId; }
            public void setRoundId(Integer roundId) { this.roundId = roundId; }

            public Integer getStateId() { return stateId; }
            public void setStateId(Integer stateId) { this.stateId = stateId; }

            public Integer getVenueId() { return venueId; }
            public void setVenueId(Integer venueId) { this.venueId = venueId; }

            public String getName() { return name; }
            public void setName(String name) { this.name = name; }

            public String getStartingAt() { return startingAt; }
            public void setStartingAt(String startingAt) { this.startingAt = startingAt; }

            public String getResultInfo() { return resultInfo; }
            public void setResultInfo(String resultInfo) { this.resultInfo = resultInfo; }

            public String getLeg() { return leg; }
            public void setLeg(String leg) { this.leg = leg; }

            public String getDetails() { return details; }
            public void setDetails(String details) { this.details = details; }

            public Integer getLength() { return length; }
            public void setLength(Integer length) { this.length = length; }

            public Boolean getPlaceholder() { return placeholder; }
            public void setPlaceholder(Boolean placeholder) { this.placeholder = placeholder; }

            public Boolean getHasOdds() { return hasOdds; }
            public void setHasOdds(Boolean hasOdds) { this.hasOdds = hasOdds; }

            public Boolean getHasPremiumOdds() { return hasPremiumOdds; }
            public void setHasPremiumOdds(Boolean hasPremiumOdds) { this.hasPremiumOdds = hasPremiumOdds; }

            public Long getStartingAtTimestamp() { return startingAtTimestamp; }
            public void setStartingAtTimestamp(Long startingAtTimestamp) { this.startingAtTimestamp = startingAtTimestamp; }

            public League getLeague() { return league; }
            public void setLeague(League league) { this.league = league; }

            public State getState() { return state; }
            public void setState(State state) { this.state = state; }
    }

    public List<ApiResponse> getData() { return data; }
    public void setData(List<ApiResponse> data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
