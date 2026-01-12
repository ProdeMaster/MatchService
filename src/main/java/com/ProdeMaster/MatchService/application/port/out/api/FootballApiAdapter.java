package com.ProdeMaster.MatchService.application.port.out.api;

import com.ProdeMaster.MatchService.application.dto.MatchDto;

import java.time.LocalDateTime;
import java.util.List;

public interface FootballApiAdapter {

    List<MatchDto> getWeeklyMatches();

    LocalDateTime parseToLocalDateTime(String date);
}
