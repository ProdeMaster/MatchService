package com.ProdeMaster.MatchService.application.port.in.web;

import com.ProdeMaster.MatchService.application.dto.MatchDto;

public interface GetMatchUseCase {
    MatchDto getMatchById(Long matchId);
}