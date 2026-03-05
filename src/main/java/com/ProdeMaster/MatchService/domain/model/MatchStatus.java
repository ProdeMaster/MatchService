package com.ProdeMaster.MatchService.domain.model;

public enum MatchStatus {
    // Not Started
    NS,
    INPLAY_1ST_HALF,
    // Half Time
    HT,
    BREAK,
    // Full Time
    FT,
    // Extra Time
    INPLAY_ET,
    // After Extra Time
    AET,
    // After Penalties
    FT_PEN,
    INPLAY_PENALTIES,
    POSTPONED,
    SUSPENDED,
    CANCELLED,
    // To Be Announced
    TBA,
    // Walk Over
    WO,
    ABANDONED,
    DELAYED,
    AWARDED,
    INTERRUPTED,
    AWAITING_UPDATES,
    DELETED,
    EXTRA_TIME_BREAK,
    INPLAY_2ND_HALF,
    INPLAY_ET_2ND_HALF,
    PEN_BREAK,
    PENDING;
}