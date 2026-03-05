package com.ProdeMaster.MatchService.application.port.in.event;

import com.ProdeMaster.MatchService.application.dto.MatchEventDTO;

public interface MatchEventHandler {
    void handleMatchCreatedEvent(MatchEventDTO event);
    void handleMatchUpdatedEvent(MatchEventDTO event);
    void handleMatchStartedEvent(MatchEventDTO event);
    void handleMatchFinishedEvent(MatchEventDTO event);
}