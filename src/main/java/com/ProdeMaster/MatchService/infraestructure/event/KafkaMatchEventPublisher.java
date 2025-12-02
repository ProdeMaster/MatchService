package com.ProdeMaster.MatchService.infraestructure.event;

import com.ProdeMaster.MatchService.domain.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.domain.event.MatchUpdatedEvent;

public class KafkaMatchEventPublisher implements MatchEventPublisher {
    public void publish(MatchUpdatedEvent event) {
    }
}
