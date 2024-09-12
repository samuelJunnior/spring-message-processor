package dev.samueljunnior.module.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProducerMessagesService {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final FanoutExchange fanoutExchange;
    private final ObjectMapper objectMapper;

    public void sendDirectMessage(String routingKey, Object message) {
        this.sendDirectMessageWithHeader(routingKey, message, null);
    }

    public void sendFanoutMessage(Object message){
        this.sendFanoutMessageWithHeader(message, null);
    }

    @SneakyThrows
    public void sendDirectMessageWithHeader(String routingKey, Object message, Map<String, Object> headers) {
        final var messageProperties = new MessageProperties();

        if(Objects.nonNull(headers)){
            messageProperties.setHeaders(headers);
        }

        final var body = objectMapper.writeValueAsString(message);
        final var messageAmq = new Message(body.getBytes(), messageProperties);
        rabbitTemplate.send(directExchange.getName(), routingKey , messageAmq);
    }

    @SneakyThrows
    public void sendFanoutMessageWithHeader(Object message, Map<String, Object> headers){
        final var messageProperties = new MessageProperties();

        if(Objects.nonNull(headers)){
            messageProperties.setHeaders(headers);
        }
        final var body = objectMapper.writeValueAsString(message);
        final var messageAmq = new Message(body.getBytes(), messageProperties);
        rabbitTemplate.send(fanoutExchange.getName(), "", messageAmq);
    }
}
