package com.ProdeMaster.MatchService.application.port.in.event;

import com.ProdeMaster.MatchService.application.service.MatchUpdatedEvent;

public interface MatchEventPublisher {
    void publish(MatchUpdatedEvent event);
}