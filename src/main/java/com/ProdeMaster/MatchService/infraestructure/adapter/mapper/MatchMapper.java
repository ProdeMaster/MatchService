package com.ProdeMaster.MatchService.infraestructure.adapter.mapper;

import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    @NonNull
    public MatchEntity toEntity(@NonNull Match match, Long homeTeamID, Long awayTeamID) {
        MatchEntity newMatch = new MatchEntity();

        newMatch.setId(match.getMatchId());
        newMatch.setProviderId(1L); // SportMonks provider ID
        newMatch.setExternalMatchId(match.getMatchId()); // SportMonks match ID
        newMatch.setStartingAt(match.getMatchDateTime());
        newMatch.setStatusId(mapMatchStatusToStatusId(match.getStatus()));

        if (match.getHomeTeam() != null && match.getAwayTeam() != null) {
            newMatch.setHomeTeamId(homeTeamID);
            newMatch.setAwayTeamId(awayTeamID);
        }

        if (match.getHomeTeamScore() != null && match.getAwayTeamScore() != null) {
            newMatch.setAwayTeamScore(match.getAwayTeamScore());
            newMatch.setHomeTeamScore(match.getHomeTeamScore());
        }

        return newMatch;
    }

    @NonNull
    public Match toDomain(@NonNull MatchEntity entity) {
        String homeTeam = "Unknown";
        String awayTeam = "Unknown";

        Integer homeScore = entity.getHomeTeamScore();
        Integer awayScore = entity.getAwayTeamScore();

        MatchStatus status = mapStatusIdToMatchStatus(entity.getStatusId());

        Match match = Match.reconstitute(
                entity.getId(),
                homeTeam,
                awayTeam,
                null,
                entity.getStartingAt(),
                status,
                null,
                homeScore,
                awayScore,
                false
        );

        if (match == null) {
            throw new IllegalArgumentException("Argumento invalido: Match reconstituida no puede ser nula");
        }
        return match;
    }

    @Nullable
    public Match toDomainNullable(@Nullable MatchEntity entity) {
        if (entity == null) {
            return null;
        }
        return toDomain(entity);
    }

    static public Long mapMatchStatusToStatusId(MatchStatus status) {
        if (status == null) {
            return 1L;
        }

        return switch (status) {
            case PENDING -> 0L;
            case TBA -> 1L;
            case NS -> 1L;
            case INPLAY_1ST_HALF -> 2L;
            case HT -> 3L;
            case INPLAY_2ND_HALF -> 4L;
            case FT -> 5L;
            case INPLAY_ET -> 6L;
            case EXTRA_TIME_BREAK -> 7L;
            case INPLAY_ET_2ND_HALF -> 8L;
            case AET -> 9L;
            case INPLAY_PENALTIES -> 10L;
            case PEN_BREAK -> 11L;
            case FT_PEN -> 12L;
            case DELAYED -> 13L;
            case POSTPONED -> 14L;
            case SUSPENDED -> 15L;
            case INTERRUPTED -> 16L;
            case AWARDED -> 17L;
            case WO -> 18L;
            case CANCELLED -> 19L;
            case ABANDONED -> 20L;
            case DELETED -> 21L;
            case AWAITING_UPDATES -> 22L;
            default -> 1L;
        };
    }

    private MatchStatus mapStatusIdToMatchStatus(Long statusId) {
        if (statusId == null) {
            return MatchStatus.NS;
        }

        return switch (statusId.intValue()) {
            case 0 -> MatchStatus.PENDING;
            case 1 -> MatchStatus.NS;
            case 2 -> MatchStatus.INPLAY_1ST_HALF;
            case 3 -> MatchStatus.HT;
            case 4 -> MatchStatus.INPLAY_2ND_HALF;
            case 5 -> MatchStatus.FT;
            case 6 -> MatchStatus.INPLAY_ET;
            case 7 -> MatchStatus.EXTRA_TIME_BREAK;
            case 8 -> MatchStatus.INPLAY_ET_2ND_HALF;
            case 9 -> MatchStatus.AET;
            case 10 -> MatchStatus.INPLAY_PENALTIES;
            case 11 -> MatchStatus.PEN_BREAK;
            case 12 -> MatchStatus.FT_PEN;
            case 13 -> MatchStatus.DELAYED;
            case 14 -> MatchStatus.POSTPONED;
            case 15 -> MatchStatus.SUSPENDED;
            case 16 -> MatchStatus.INTERRUPTED;
            case 17 -> MatchStatus.AWARDED;
            case 18 -> MatchStatus.WO;
            case 19 -> MatchStatus.CANCELLED;
            case 20 -> MatchStatus.ABANDONED;
            case 21 -> MatchStatus.DELETED;
            case 22 -> MatchStatus.AWAITING_UPDATES;
            default -> MatchStatus.NS;
        };
    }
}
