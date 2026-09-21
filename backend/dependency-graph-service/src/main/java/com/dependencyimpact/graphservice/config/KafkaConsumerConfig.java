package com.dependencyimpact.graphservice.config;

import com.dependencyimpact.common.exceptions.NonRetryableEventException;
import com.dependencyimpact.graphservice.kafka.KafkaRetryProperties;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

// Wires retry + DLQ for every @KafkaListener in this service, per
// docs/kafka-spec.md sections 31-37. A single CommonErrorHandler (DefaultErrorHandler
// is one) bean is auto-detected by Spring Boot's Kafka autoconfiguration and applied
// to the listener container factory - no need to hand-build the whole factory.
@Configuration
@EnableConfigurationProperties(KafkaRetryProperties.class)
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, String> kafkaTemplate,
                                                  KafkaRetryProperties retryProperties) {
        // -1 lets Kafka pick a partition for the DLQ record, rather than reusing the
        // original partition number, which may not exist on the (separately
        // auto-created) *-dlq topic if partition counts differ.
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + "-dlq", -1));

        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(retryProperties.getMaxRetries());
        backOff.setInitialInterval(retryProperties.getInitialIntervalMs());
        backOff.setMultiplier(retryProperties.getMultiplier());

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);
        // Skip straight to the DLQ for messages that will never parse successfully,
        // no matter how many times they're retried.
        errorHandler.addNotRetryableExceptions(NonRetryableEventException.class);
        return errorHandler;
    }
}
