package com.ProdeMaster.MatchService.Service.Adapters;

import com.ProdeMaster.MatchService.Dto.MatchDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface FootballApiAdapter {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterToDataTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    List<MatchDto> getWeeklyMatches ();
    LocalDateTime parseToLocalDateTime (String date);
}
