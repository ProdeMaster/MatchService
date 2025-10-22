package com.ProdeMaster.MatchService.domain.event;

public interface MatchEventPublisher {
    void publish(MatchUpdatedEvent event);
}