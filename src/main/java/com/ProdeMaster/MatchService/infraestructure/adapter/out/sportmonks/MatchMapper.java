package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks;

import com.ProdeMaster.MatchService.domain.model.Match;
import com.ProdeMaster.MatchService.domain.model.MatchStatus;
import com.ProdeMaster.MatchService.infraestructure.entity.MatchEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between Match domain objects and MatchEntity persistence
 * objects.
 * Acts as an anti-corruption layer to maintain hexagonal architecture.
 * 
 * This mapper bridges the impedance mismatch between:
 * - Domain model: Match with business logic and MatchStatus state machine
 * - Persistence model: MatchEntity aligned with external API structure
 */
@Component
public class MatchMapper {

    /**
     * Maps from domain Match to persistence MatchEntity.
     * 
     * Note: This mapping is lossy - the new MatchEntity structure doesn't have
     * all the fields from the domain Match. This is intentional as MatchEntity
     * is designed to store data from external API, not domain state.
     */
    @NonNull
    public MatchEntity toEntity(@NonNull Match match) {
        MatchEntity entity = new MatchEntity();

        // Map ID
        entity.setId(match.getMatchId());

        // Map temporal fields
        entity.setStartingAt(match.getMatchDateTime());
        if (match.getMatchDateTime() != null) {
            entity.setStartingAtTimestamp(
                    match.getMatchDateTime().atZone(java.time.ZoneId.systemDefault()).toEpochSecond());
        }

        // Map state - convert MatchStatus enum to Integer stateId
        // This is a simplified mapping - you may need to adjust based on your state ID
        // scheme
        entity.setStateId(mapMatchStatusToStateId(match.getStatus()));

        // Map match name from teams (domain uses homeTeam/awayTeam, persistence uses
        // name)
        if (match.getHomeTeam() != null && match.getAwayTeam() != null) {
            entity.setName(match.getHomeTeam() + " vs " + match.getAwayTeam());
        }

        // Map league - domain uses String, persistence uses Integer ID
        // For now, we'll leave leagueId null as we don't have the mapping
        // This should be populated when fetching from external API
        if (match.getLeague() != null) {
            // TODO: Implement league name to ID mapping if needed
            // entity.setLeagueId(mapLeagueNameToId(match.getLeague()));
        }

        // Map result info from scores
        if (match.getHomeTeamScore() != null && match.getAwayTeamScore() != null) {
            entity.setResultInfo(match.getHomeTeamScore() + " - " + match.getAwayTeamScore());
        }

        // Set default values for API-specific fields
        entity.setPlaceholder(false);
        entity.setHasOdds(false);
        entity.setHasPremiumOdds(false);

        return entity;
    }

    /**
     * Maps from persistence MatchEntity to domain Match.
     * 
     * This reconstructs the domain model from the persistence structure.
     * Some domain fields may need to be derived or defaulted.
     */
    @NonNull
    public Match toDomain(@NonNull MatchEntity entity) {
        // Extract home and away teams from name field
        String homeTeam = "Unknown";
        String awayTeam = "Unknown";
        if (entity.getName() != null && entity.getName().contains(" vs ")) {
            String[] teams = entity.getName().split(" vs ");
            if (teams.length == 2) {
                homeTeam = teams[0].trim();
                awayTeam = teams[1].trim();
            }
        }

        // Extract scores from resultInfo
        Integer homeScore = null;
        Integer awayScore = null;
        if (entity.getResultInfo() != null && entity.getResultInfo().contains(" - ")) {
            try {
                String[] scores = entity.getResultInfo().split(" - ");
                if (scores.length == 2) {
                    homeScore = Integer.parseInt(scores[0].trim());
                    awayScore = Integer.parseInt(scores[1].trim());
                }
            } catch (NumberFormatException e) {
                // If parsing fails, leave scores as null
            }
        }

        // Map state ID to MatchStatus
        MatchStatus status = mapStateIdToMatchStatus(entity.getStateId());

        // Reconstruct domain Match
        Match match = Match.reconstitute(
                entity.getId(),
                homeTeam,
                awayTeam,
                entity.getLeague() != null ? entity.getLeague().getName() : null,
                entity.getStartingAt(),
                status,
                null, // previousStatus - not stored in new entity structure
                homeScore,
                awayScore,
                false // resultConfirmed - not stored in new entity structure
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

    /**
     * Maps MatchStatus enum to state ID.
     * 
     * This is a simplified mapping. You should adjust based on your actual
     * state ID scheme from the external API.
     */
    private Integer mapMatchStatusToStateId(MatchStatus status) {
        if (status == null) {
            return 1; // Default to NS (Not Started)
        }

        // Simple mapping - adjust based on your actual API state IDs
        return switch (status) {
            case PENDING -> 0;
            case TBA -> 1;
            case NS -> 1;
            case INPLAY_1ST_HALF -> 2;
            case HT -> 3;
            case INPLAY_2ND_HALF -> 4;
            case FT -> 5;
            case INPLAY_ET -> 6;
            case EXTRA_TIME_BREAK -> 7;
            case INPLAY_ET_2ND_HALF -> 8;
            case AET -> 9;
            case INPLAY_PENALTIES -> 10;
            case PEN_BREAK -> 11;
            case FT_PEN -> 12;
            case DELAYED -> 13;
            case POSTPONED -> 14;
            case SUSPENDED -> 15;
            case INTERRUPTED -> 16;
            case AWARDED -> 17;
            case WO -> 18;
            case CANCELLED -> 19;
            case ABANDONED -> 20;
            case DELETED -> 21;
            case AWAITING_UPDATES -> 22;
            default -> 1; // Default to NS state
        };
    }

    /**
     * Maps state ID to MatchStatus enum.
     * 
     * This is the reverse of mapMatchStatusToStateId.
     */
    private MatchStatus mapStateIdToMatchStatus(Integer stateId) {
        if (stateId == null) {
            return MatchStatus.NS;
        }

        // Simple mapping - adjust based on your actual API state IDs
        return switch (stateId) {
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
