package com.ProdeMaster.MatchService.infraestructure.cache;

import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.domain.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class ValkeyMatchCacheRepository implements MatchCacheRepository {

    private static final Logger log = LoggerFactory.getLogger(ValkeyMatchCacheRepository.class);
    private static final String MATCH_KEY_PREFIX = "match:";
    private static final Duration DEFAULT_TTL = Duration.ofHours(1);
    private static final Duration LIVE_MATCH_TTL = Duration.ofMinutes(5);

    private final ReactiveRedisTemplate<String, Match> redisTemplate;

    public ValkeyMatchCacheRepository(ReactiveRedisTemplate<String, Match> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Match> getMatch(Long matchId) {
        String key = buildKey(matchId);
        log.debug("Getting match from cache: {}", key);
        return redisTemplate.opsForValue().get(key)
                .doOnNext(match -> log.debug("Cache hit for match: {}", matchId))
                .doOnError(error -> log.error("Error getting match from cache: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> cacheMatch(Match match) {
        return cacheMatch(match, getTtlForMatch(match));
    }

    @Override
    public Mono<Void> cacheMatch(Match match, Duration ttl) {
        if (match == null || match.getMatchId() == null) {
            return Mono.empty();
        }

        String key = buildKey(match.getMatchId());
        log.debug("Caching match: {} with TTL: {} seconds", key, ttl.getSeconds());

        return redisTemplate.opsForValue()
                .set(key, match, ttl)
                .doOnSuccess(success -> log.debug("Match cached successfully: {}", match.getMatchId()))
                .doOnError(error -> log.error("Error caching match: {}", error.getMessage()))
                .then();
    }

    @Override
    public Mono<Void> updateCachedMatch(Match match) {
        return cacheMatch(match);
    }

    @Override
    public Mono<Void> evictMatch(Long matchId) {
        String key = buildKey(matchId);
        log.debug("Evicting match from cache: {}", key);
        return redisTemplate.delete(key)
                .doOnSuccess(count -> log.debug("Match evicted: {}", matchId))
                .doOnError(error -> log.error("Error evicting match: {}", error.getMessage()))
                .then();
    }

    @Override
    public Mono<Boolean> exists(Long matchId) {
        String key = buildKey(matchId);
        return redisTemplate.hasKey(key)
                .doOnNext(exists -> log.debug("Match {} exists in cache: {}", matchId, exists));
    }

    private String buildKey(Long matchId) {
        return MATCH_KEY_PREFIX + matchId;
    }

    private Duration getTtlForMatch(Match match) {
        if (match.getStatus() == null) {
            return DEFAULT_TTL;
        }

        return switch (match.getStatus()) {
            case INPLAY_1ST_HALF, INPLAY_2ND_HALF, INPLAY_ET, INPLAY_ET_2ND_HALF, INPLAY_PENALTIES,
                 HT, EXTRA_TIME_BREAK, PEN_BREAK -> LIVE_MATCH_TTL;
            default -> DEFAULT_TTL;
        };
    }
}
