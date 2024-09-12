package dev.samueljunnior.config.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    @Value("${rabbit.retries}")
    private int retries;

    @Value("${rabbit.back-off-period}")
    private long backOffPeriod;

   private final DirectExchange directExchange;

    @Bean
    public MessageRecoverer dlqMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return (message, cause) -> {
            final var originalQueue = message.getMessageProperties().getConsumerQueue();
            message.getMessageProperties().getHeaders().put(RabbitMQConstants.HEADER_ORIGINAL_QUEUE, originalQueue);
            message.getMessageProperties().getHeaders().put(RabbitMQConstants.HEADER_REASON, cause.getMessage());
            rabbitTemplate.send(directExchange.getName(), RabbitMQConstants.DLQ_QUEUE_NAME, message);
        };
    }

    @Bean
    public RetryOperationsInterceptor customRetryInterceptor(MessageRecoverer dlqMessageRecoverer){
        return RetryInterceptorBuilder.stateless()
                .maxAttempts(retries)
                .backOffOptions(backOffPeriod, 1.0, backOffPeriod)
                .recoverer(dlqMessageRecoverer)
                .build();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory customerContainerFactory(
            ConnectionFactory connectionFactory,
            RetryOperationsInterceptor customRetryInterceptor
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(customRetryInterceptor);

        return factory;
    }

}

