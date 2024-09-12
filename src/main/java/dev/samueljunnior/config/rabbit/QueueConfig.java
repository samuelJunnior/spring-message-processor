package dev.samueljunnior.config.rabbit;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {

    @Value("${rabbit.ttl}")
    private int ttl;

    @Value("${rabbit.exchanges.direct.name}")
    private String direcatExchangeName;

    @Bean
    public Queue deadLeatterQueue(){
        return QueueBuilder.durable(RabbitMQConstants.DLQ_QUEUE_NAME)
                .ttl(ttl)
                .build();
    }

    @Bean
    public Queue initialAnalysisQueue(){
        return QueueBuilder.durable(RabbitMQConstants.PRIMARY_QUEUE_NAME)
                .deadLetterExchange(direcatExchangeName)
                .deadLetterRoutingKey(RabbitMQConstants.DLQ_QUEUE_NAME)
                .build();
    }

    @Bean
    public Queue secondaryAnalysisQueue() {
        return QueueBuilder.durable(RabbitMQConstants.SECONDARY_QUEUE_NAME)
                .deadLetterExchange(direcatExchangeName)
                .deadLetterRoutingKey(RabbitMQConstants.DLQ_QUEUE_NAME)
                .build();
    }

    @Bean
    public Queue tertiaryAnalysisQueue() {
        return QueueBuilder.durable(RabbitMQConstants.TERTIARY_QUEUE_NAME)
                .deadLetterExchange(direcatExchangeName)
                .deadLetterRoutingKey(RabbitMQConstants.DLQ_QUEUE_NAME)
                .build();
    }
}
