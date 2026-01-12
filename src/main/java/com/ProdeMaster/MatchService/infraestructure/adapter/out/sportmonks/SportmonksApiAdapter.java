package com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks;

import com.ProdeMaster.MatchService.application.dto.MatchDto;
import com.ProdeMaster.MatchService.application.port.out.api.FootballApiAdapter;
import com.ProdeMaster.MatchService.infraestructure.adapter.out.sportmonks.dto.SportmonksApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SportmonksApiAdapter implements FootballApiAdapter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestClient restClient;

    @Value("${sportmonks.token}")
    private String token;

    @Autowired
    public SportmonksApiAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<MatchDto> getWeeklyMatches() {
        LocalDate today = LocalDate.now();
        String todayFormatted = today.format(DATE_FORMATTER);

        LocalDate week = today.plusWeeks(1);
        String weekFormatted = week.format(DATE_FORMATTER);

        SportmonksApiResponse response = restClient
                .get()
                .uri("/football/fixtures/between/{start}/{end}?api_token={token}&include=league;state&per_page=50",
                        Map.of("start", todayFormatted, "end", weekFormatted, "token", token))
                .retrieve()
                .body(SportmonksApiResponse.class);

        List<MatchDto> matches = new ArrayList<>();

        if (response != null && response.getFixtures() != null) {
            for (var fixture : response.getFixtures()) {
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

    public LocalDateTime parseToLocalDateTime(String date) {
        return LocalDateTime.parse(date, DATETIME_FORMATTER);
    }
}
