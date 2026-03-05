package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks;

import com.ProdeMaster.MatchService.application.dto.MatchDto;
import com.ProdeMaster.MatchService.application.port.out.api.FootballApiAdapter;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto.SportmonksApiResponse;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto.SportmonksFixtureResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class SportmonksApiAdapter implements FootballApiAdapter {

    private static final Logger log = LoggerFactory.getLogger(SportmonksApiAdapter.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestClient restClient;

    private final String token;
    private final String baseUrl;

    @Autowired
    public SportmonksApiAdapter(RestClient restClient,
            @Value("${sportmonks.token}") String token,
            @Value("${sportmonks.base_url}") String baseUrl) {
        if (token == null || token.isEmpty() || baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalArgumentException("Sportmonks token or base url is required");
        }
        this.restClient = restClient;
        this.token = token;
        this.baseUrl = baseUrl;
    }

    @Override
    public List<MatchDto> getWeeklyMatches() {
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DATE_FORMATTER);
        LocalDate week = today.plusWeeks(1);
        String weekFormatted = week.format(DATE_FORMATTER);

        String uri = baseUrl + "/football/fixtures/between/{start}/{end}?api_token={token}";

        try {
            SportmonksApiResponse response = SportmonksRetryInterceptor.executeWithRetry(
                    () -> restClient
                            .get()
                            .uri(uri, Map.of("start", todayFormatted, "end", weekFormatted, "token", token))
                            .retrieve()
                            .body(SportmonksApiResponse.class),
                    "getWeeklyMatches");

            return mapFixturesToMatchDtos(response);
        } catch (Exception e) {
            log.error("Error getting weekly matches: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<MatchDto> getTodayMatches() {
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DATE_FORMATTER);

        String uri = baseUrl + "/football/fixtures/date/{today}?api_token={token}";

        try {
            SportmonksApiResponse response = SportmonksRetryInterceptor.executeWithRetry(
                    () -> restClient
                            .get()
                            .uri(uri, Map.of("today", todayFormatted, "token", token))
                            .retrieve()
                            .body(SportmonksApiResponse.class),
                    "getTodayMatches");

            return mapFixturesToMatchDtos(response);
        } catch (Exception e) {
            log.error("Error getting today matches: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<MatchDto> getMatchesByLeague(Long leagueId) {
        String uri = baseUrl + "/football/fixtures?api_token={token}&filters=fixtureLeagues:{leagueId}";

        try {
            SportmonksApiResponse response = SportmonksRetryInterceptor.executeWithRetry(
                    () -> restClient
                            .get()
                            .uri(uri, Map.of("leagueId", leagueId, "token", token))
                            .retrieve()
                            .body(SportmonksApiResponse.class),
                    "getMatchesByLeague");

            return mapFixturesToMatchDtos(response);
        } catch (Exception e) {
            log.error("Error getting matches by league {}: {}", leagueId, e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<MatchDto> getMatchesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String startFormatted = startDate.format(DATE_FORMATTER);
        String endFormatted = endDate.format(DATE_FORMATTER);

        String uri = baseUrl + "/football/fixtures/between/{start}/{end}?api_token={token}";

        try {
            SportmonksApiResponse response = SportmonksRetryInterceptor.executeWithRetry(
                    () -> restClient
                            .get()
                            .uri(uri, Map.of("start", startFormatted, "end", endFormatted, "token", token))
                            .retrieve()
                            .body(SportmonksApiResponse.class),
                    "getMatchesByDateRange");

            return mapFixturesToMatchDtos(response);
        } catch (Exception e) {
            log.error("Error getting matches by date range: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Maps the response from Sportmonks API to a list of MatchDto objects.
     *
     * @param response the response from Sportmonks API containing fixture data
     * @return a list of MatchDto objects representing the matches
     */
    private List<MatchDto> mapFixturesToMatchDtos(SportmonksApiResponse response) {
        List<MatchDto> matches = new ArrayList<>();

        if (response != null && response.getFixtures() != null) {
            for (SportmonksFixtureResponse.FixtureData fixture : response.getFixtures()) {
                String[] teams = fixture.getName().split(" vs ");
                String homeTeam = teams.length > 0 ? teams[0].trim() : "Unknown";
                String awayTeam = teams.length > 1 ? teams[1].trim() : "Unknown";

                matches.add(new MatchDto(
                        fixture.getId(),
                        homeTeam,
                        awayTeam,
                        null,
                        fixture.getStartingAt(),
                        null));
            }
        }

        return matches;
    }

    @Override
    public LocalDateTime parseToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DATETIME_FORMATTER);
    }
}
