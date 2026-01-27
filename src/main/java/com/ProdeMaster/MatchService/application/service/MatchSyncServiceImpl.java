package com.ProdeMaster.MatchService.application.service;

import com.ProdeMaster.MatchService.application.dto.MatchDto;
import com.ProdeMaster.MatchService.application.port.in.sync.MatchSyncService;
import com.ProdeMaster.MatchService.application.port.out.api.FootballApiAdapter;
import com.ProdeMaster.MatchService.application.port.out.cache.MatchCacheRepository;
import com.ProdeMaster.MatchService.application.port.out.db.MatchRepository;
import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MatchSyncServiceImpl implements MatchSyncService {

    private static final Logger log = LoggerFactory.getLogger(MatchSyncServiceImpl.class);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // For the MVP, the Sportmonks ID is hardcoded, the sole provider.
    private static final Long SPORTMONKS_PROVIDER_ID = 1L;

    private final FootballApiAdapter apiAdapter;
    private final MatchRepository matchRepository;
    private final MatchCacheRepository cacheRepository;

    public MatchSyncServiceImpl(FootballApiAdapter apiAdapter,
            MatchRepository matchRepository,
            MatchCacheRepository cacheRepository) {
        this.apiAdapter = apiAdapter;
        this.matchRepository = matchRepository;
        this.cacheRepository = cacheRepository;
    }

    @Override
    public List<Match> syncWeeklyMatches() {
        log.info("Starting weekly matches sync from SportMonks");
        List<MatchDto> fixtures = apiAdapter.getWeeklyMatches();

        int synced = 0;
        int skipped = 0;

        for (MatchDto fixture : fixtures) {
            Long sportmonksMatchId = fixture.getId();

            // Buscar por providerId + externalMatchId para SportMonks
            Optional<Match> existing = matchRepository.findByProviderIdAndExternalMatchId(SPORTMONKS_PROVIDER_ID,
                    sportmonksMatchId);
            if (existing.isPresent()) {
                log.debug("Match {} already exists for provider {}, skipping", sportmonksMatchId,
                        SPORTMONKS_PROVIDER_ID);
                skipped++;
                continue;
            }

            Match match = createMatchFromDto(fixture, sportmonksMatchId);
            Match saved = matchRepository.save(match);
            cacheRepository.cacheMatch(saved).block();
            synced++;
            log.debug("Synced match: {} - {} vs {}", sportmonksMatchId, fixture.getHomeTeam(), fixture.getAwayTeam());
        }

        log.info("Weekly sync completed: {} synced, {} skipped", synced, skipped);
        return matchRepository.findAll();
    }

    @Override
    public Match syncMatchById(Long sportmonksMatchId) {
        log.info("Syncing match by SportMonks ID: {}", sportmonksMatchId);

        // Buscar por providerId + externalMatchId
        Optional<Match> existing = matchRepository.findByProviderIdAndExternalMatchId(SPORTMONKS_PROVIDER_ID,
                sportmonksMatchId);
        if (existing.isPresent()) {
            log.debug("Match {} already exists for provider {}, returning cached version", sportmonksMatchId,
                    SPORTMONKS_PROVIDER_ID);
            return existing.get();
        }

        // Buscar el fixture específico en lugar de toda la lista
        List<MatchDto> matches = apiAdapter.getWeeklyMatches();
        Optional<MatchDto> fixture = matches.stream()
                .filter(m -> m.getId().equals(sportmonksMatchId))
                .findFirst();

        if (fixture.isEmpty()) {
            throw new IllegalArgumentException("Match not found in SportMonks: " + sportmonksMatchId);
        }

        Match match = createMatchFromDto(fixture.get(), sportmonksMatchId);
        Match saved = matchRepository.save(match);
        cacheRepository.cacheMatch(saved).block();

        log.info("Match {} synced and saved with provider {}", sportmonksMatchId, SPORTMONKS_PROVIDER_ID);
        return saved;
    }

    @Override
    public void syncAndPersistFromSportMonks() {
        syncWeeklyMatches();
    }

    private Match createMatchFromDto(MatchDto dto, Long sportmonksMatchId) {
        LocalDateTime matchDateTime = null;
        if (dto.getMatchDateTime() != null) {
            try {
                matchDateTime = apiAdapter.parseToLocalDateTime(dto.getMatchDateTime());
            } catch (Exception e) {
                log.warn("Could not parse date: {}", dto.getMatchDateTime());
            }
        }

        MatchStatus status = MatchStatus.NS;
        if (dto.getStatus() != null) {
            try {
                status = MatchStatus.valueOf(dto.getStatus());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown status: {}", dto.getStatus());
            }
        }

        return new Match(
                null, // ID interno será autogenerado por la BD
                dto.getHomeTeam(),
                dto.getAwayTeam(),
                dto.getLeague(),
                matchDateTime != null ? matchDateTime : LocalDateTime.now().plusDays(1));
    }
}
