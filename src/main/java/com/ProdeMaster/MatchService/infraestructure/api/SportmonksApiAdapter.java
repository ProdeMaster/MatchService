package com.ProdeMaster.MatchService.infraestructure.api;

import com.ProdeMaster.MatchService.application.dto.MatchDto;
import com.ProdeMaster.MatchService.application.port.out.api.FootballApiAdapter;
import com.ProdeMaster.MatchService.infraestructure.api.dto.SportmonksApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SportmonksApiAdapter implements FootballApiAdapter {

    private final RestClient restClient;

    @Value("${sportmonks.token}")
    private String token;

    @Autowired
    public SportmonksApiAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<MatchDto> getWeeklyMatches (){
        // Fecha de prueba, en las que me aseguro que devuelva al menos 1 partido
        String todayFormatter = "2025-05-25";
        String weekFormatter = "2025-06-01";

        /**
         LocalDate today = LocalDate.now();
         String todayFormatter = today.format(formatter);

         LocalDate week = today.plusWeeks(1);
         String weekFormatter = week.format(formatter);
         */

        SportmonksApiResponse sportmonksApiResponse = restClient
                .get()
                .uri("/football/fixtures/between/{todayFormatter}/{weekFormatter}?api_token={api_token}&include=league;state", Map.of("todayFormatter", todayFormatter, "weekFormatter", weekFormatter, "api_token", token))
                .retrieve()
                .body(SportmonksApiResponse.class);

        List<MatchDto> weekMatches = new ArrayList<>();

        for (SportmonksApiResponse.ApiResponse match : sportmonksApiResponse.getData()) {
            String[] teams = match.getName().split(" vs ");
            weekMatches.add(new MatchDto(
                    match.getId(),
                    teams[0],
                    teams[1],
                    match.getLeague().getName(),
                    match.getStartingAt(),
                    match.getState().getName()
            ));
        }
        return weekMatches;
    }
    
    public LocalDateTime parseToLocalDateTime (String date) {
        return LocalDateTime.parse(date, formatterToDataTime);
    }
}
