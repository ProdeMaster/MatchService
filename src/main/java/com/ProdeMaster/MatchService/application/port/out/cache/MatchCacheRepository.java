package com.ProdeMaster.MatchService.application.port.out.cache;

import com.ProdeMaster.MatchService.domain.model.Match;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface MatchCacheRepository {

    Mono<Match> getMatch(Long matchId);

    Mono<Void> cacheMatch(Match match);

    Mono<Void> cacheMatch(Match match, Duration ttl);

    Mono<Void> updateCachedMatch(Match match);

    Mono<Void> evictMatch(Long matchId);

    Mono<Boolean> exists(Long matchId);
}
