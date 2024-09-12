package dev.samueljunnior.module.message.service;

import dev.samueljunnior.config.rabbit.RabbitMQConstants;
import dev.samueljunnior.module.message.dto.NewMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ProducerMessagesService producerMessagesService;

    public void newMessge(NewMessageDTO dto) {
        producerMessagesService.sendDirectMessage(RabbitMQConstants.PRIMARY_QUEUE_NAME, dto);
    }
}
