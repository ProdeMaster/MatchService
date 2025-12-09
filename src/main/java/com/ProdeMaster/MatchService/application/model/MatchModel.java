package com.ProdeMaster.MatchService.application.model;

import java.time.LocalDateTime;

public class MatchModel {
    private Long id;
    private Integer sportId;
    private Integer leagueId;
    private Integer seasonId;
    private Long stageId;
    private Long groupId;
    private Long aggregateId;
    private Long roundId;
    private Integer stateId;
    private Integer venueId;
    private String name;
    private LocalDateTime startingAt;
    private String resultInfo;
    private String leg;
    private String details;
    private Integer length;
    private Boolean placeholder;
    private Boolean hasOdds;
    private Boolean hasPremiumOdds;
    private Long startingAtTimestamp;
    private LeagueModel league;
    private StateModel state;

    // Constructors
    public MatchModel() {
    }

    public MatchModel(Long id, Integer sportId, Integer leagueId, Integer seasonId, Long stageId,
            Long groupId, Long aggregateId, Long roundId, Integer stateId, Integer venueId,
            String name, LocalDateTime startingAt, String resultInfo, String leg, String details,
            Integer length, Boolean placeholder, Boolean hasOdds, Boolean hasPremiumOdds,
            Long startingAtTimestamp, LeagueModel league, StateModel state) {
        this.id = id;
        this.sportId = sportId;
        this.leagueId = leagueId;
        this.seasonId = seasonId;
        this.stageId = stageId;
        this.groupId = groupId;
        this.aggregateId = aggregateId;
        this.roundId = roundId;
        this.stateId = stateId;
        this.venueId = venueId;
        this.name = name;
        this.startingAt = startingAt;
        this.resultInfo = resultInfo;
        this.leg = leg;
        this.details = details;
        this.length = length;
        this.placeholder = placeholder;
        this.hasOdds = hasOdds;
        this.hasPremiumOdds = hasPremiumOdds;
        this.startingAtTimestamp = startingAtTimestamp;
        this.league = league;
        this.state = state;
    }

    // Getters and Setters
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

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(Long aggregateId) {
        this.aggregateId = aggregateId;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
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

    public LocalDateTime getStartingAt() {
        return startingAt;
    }

    public void setStartingAt(LocalDateTime startingAt) {
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

    public LeagueModel getLeague() {
        return league;
    }

    public void setLeague(LeagueModel league) {
        this.league = league;
    }

    public StateModel getState() {
        return state;
    }

    public void setState(StateModel state) {
        this.state = state;
    }

}
