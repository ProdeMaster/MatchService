package com.ProdeMaster.MatchService.application.port.out.api;

import com.ProdeMaster.MatchService.application.dto.MatchDto;

import java.time.LocalDateTime;
import java.util.List;

public interface FootballApiAdapter {

    /*
     * Get the weekly matches from the API
     * 
     * @return List of MatchDto
     */
    List<MatchDto> getWeeklyMatches();

    /*
     * Parse a date string to a LocalDateTime
     * 
     * @param date String
     * 
     * @return LocalDateTime
     */
    LocalDateTime parseToLocalDateTime(String date);
}
