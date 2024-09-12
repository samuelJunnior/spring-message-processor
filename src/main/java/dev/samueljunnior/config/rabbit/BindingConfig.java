package dev.samueljunnior.config.rabbit;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BindingConfig {

    private final Queue initialAnalysisQueue;
    private final Queue secondaryAnalysisQueue;
    private final Queue tertiaryAnalysisQueue;
    private final Queue deadLeatterQueue;

    @Bean
    public Binding bindingPrimaryQueue(DirectExchange directExchange){
        return BindingBuilder.bind(initialAnalysisQueue).to(directExchange).with(initialAnalysisQueue.getName());
    }

    @Bean
    public Binding bindingSecondaryQueue(FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(secondaryAnalysisQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingTertiaryQueue(FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(tertiaryAnalysisQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindingDeadLetterQueue(DirectExchange directExchange) {
        return BindingBuilder.bind(deadLeatterQueue).to(directExchange).with(deadLeatterQueue.getName());
    }
}
