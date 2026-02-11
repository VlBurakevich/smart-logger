package com.solution.coreservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Value("${app.kafka.inference-request}")
    private String snapshotTopic;

    @Bean
    public NewTopic logsSnapshotTopic() {
        return TopicBuilder.name(snapshotTopic)
                .partitions(6)
                .replicas(1)
                .build();
    }
}
