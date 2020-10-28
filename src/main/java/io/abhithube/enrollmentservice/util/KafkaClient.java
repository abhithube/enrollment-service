package io.abhithube.enrollmentservice.util;

import io.abhithube.enrollmentservice.dto.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaClient {
    private final KafkaTemplate<String, Member> kafkaTemplate;

    @Autowired
    public KafkaClient(KafkaTemplate<String, Member> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Member member, String topic) {
        kafkaTemplate.send(topic, member);
    }
}
