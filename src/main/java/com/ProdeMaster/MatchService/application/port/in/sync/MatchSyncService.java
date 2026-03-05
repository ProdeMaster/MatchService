package com.ProdeMaster.MatchService.application.port.in.sync;

import com.ProdeMaster.MatchService.domain.model.Match;

import java.util.List;

public interface MatchSyncService {

    List<Match> syncWeeklyMatches();

    // Sync a match's by provider id
    Match syncMatchById(Long providerId);

    void syncAndPersistFromSportMonks();
}
