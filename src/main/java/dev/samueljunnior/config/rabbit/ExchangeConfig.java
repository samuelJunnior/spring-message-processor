package dev.samueljunnior.config.rabbit;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfig {

    @Value("${rabbit.exchanges.direct.name}")
    private String direcatExchangeName;

    @Value("${rabbit.exchanges.fanout.name}")
    private String fanoutExchangeName;

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(direcatExchangeName);
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(fanoutExchangeName);
    }
}

