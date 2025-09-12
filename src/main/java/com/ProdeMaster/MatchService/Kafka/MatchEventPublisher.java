package com.ProdeMaster.MatchService.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.ProdeMaster.MatchService.Model.MatchModel;

@Component
public class MatchEventPublisher {

    @Autowired
    private KafkaTemplate<String, MatchModel> kafkaTemplate;

    @Value("${app.kafka.topic.match-events}")
    private String topic;

    public void publish(MatchModel match) {
        kafkaTemplate.send(topic, match);
    }
}
