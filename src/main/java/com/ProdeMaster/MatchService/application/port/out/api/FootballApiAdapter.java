package com.ProdeMaster.MatchService.application.port.out.api;

import com.ProdeMaster.MatchService.application.dto.MatchDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface FootballApiAdapter {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterToDataTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    List<MatchDto> getWeeklyMatches ();
    LocalDateTime parseToLocalDateTime (String date);
}
