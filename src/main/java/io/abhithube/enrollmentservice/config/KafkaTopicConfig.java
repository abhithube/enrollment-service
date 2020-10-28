package io.abhithube.enrollmentservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic enrollmentTopic() {
        return new NewTopic("enrollment", 1, (short) 3);
    }

    @Bean
    public NewTopic cancellationTopic() {
        return new NewTopic("cancellation", 1, (short) 3);
    }

    @Bean
    public NewTopic paymentTopic() {
        return new NewTopic("payment", 1, (short) 3);
    }
}
