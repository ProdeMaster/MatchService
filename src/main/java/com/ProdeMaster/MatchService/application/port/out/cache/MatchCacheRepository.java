package com.ProdeMaster.MatchService.application.port.out.cache;

import com.ProdeMaster.MatchService.domain.model.Match;
import java.util.List;
import java.util.Optional;

public interface MatchCacheRepository {
    // Basic cache operations
    void cacheMatch(Match match);

    Optional<Match> getFromCache(Long matchId);

    void removeFromCache(Long matchId);

    void clearCache();

    // Cache specific operations
    List<Match> getCachedMatchesByLeague(String league);

    boolean isMatchCached(Long matchId);

    void updateCachedMatch(Match match);

    // Expiration operations
    void setExpirationTime(Long matchId, long seconds);

    // Batch operations
    void cacheMatches(List<Match> matches);

    List<Match> getAllCachedMatches();
}