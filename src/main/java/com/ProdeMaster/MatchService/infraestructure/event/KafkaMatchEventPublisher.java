package com.ProdeMaster.MatchService.infraestructure.event;

import com.ProdeMaster.MatchService.application.port.in.event.MatchEventPublisher;
import com.ProdeMaster.MatchService.application.service.MatchUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMatchEventPublisher implements MatchEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaMatchEventPublisher.class);

    private final KafkaTemplate<String, MatchUpdatedEvent> kafkaTemplate;
    private final String topic;

    public KafkaMatchEventPublisher(KafkaTemplate<String, MatchUpdatedEvent> kafkaTemplate,
                                     @Value("${app.kafka.topic.match-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(MatchUpdatedEvent event) {
        String key = event.getMatchId() != null ? event.getMatchId().toString() : "unknown";

        log.info("Publishing match event to topic {} with key {}: {}", topic, key, event.getUpdateDescription());

        CompletableFuture<SendResult<String, MatchUpdatedEvent>> future =
                kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish match event: {}", ex.getMessage(), ex);
            } else {
                log.debug("Match event published successfully to partition {} with offset {}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }
}
