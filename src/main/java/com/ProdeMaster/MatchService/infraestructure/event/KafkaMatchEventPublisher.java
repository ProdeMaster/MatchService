package com.ProdeMaster.MatchService.infraestructure.event;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.application.service.MatchUpdatedEvent;

public class KafkaMatchEventPublisher implements MatchEventPublisher {
    public void publish(MatchUpdatedEvent event) {
    }
}
