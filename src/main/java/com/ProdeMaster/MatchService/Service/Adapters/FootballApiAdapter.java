package com.ProdeMaster.MatchService.Service.Adapters;

import com.ProdeMaster.MatchService.Dto.MatchDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

public interface FootballApiAdapter {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    List<MatchDto> getWeeklyMatches ();
}
