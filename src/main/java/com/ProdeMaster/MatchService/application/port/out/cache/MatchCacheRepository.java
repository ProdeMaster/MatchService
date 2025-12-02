package com.ProdeMaster.MatchService.application.port.out.cache;

import com.ProdeMaster.MatchService.domain.model.MatchModel;
import java.util.List;
import java.util.Optional;

public interface MatchCacheRepository {
    // Operaciones básicas de caché
    void cacheMatch(MatchModel match);
    Optional<MatchModel> getFromCache(Long matchId);
    void removeFromCache(Long matchId);
    void clearCache();

    // Operaciones específicas de caché
    List<MatchModel> getCachedMatchesByLeague(String league);
    boolean isMatchCached(Long matchId);
    void updateCachedMatch(MatchModel match);

    // Operaciones de expiración
    void setExpirationTime(Long matchId, long seconds);

    // Operaciones de batch
    void cacheMatches(List<MatchModel> matches);
    List<MatchModel> getAllCachedMatches();
}