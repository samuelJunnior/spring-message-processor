package dev.samueljunnior.module.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.samueljunnior.config.rabbit.RabbitMQConstants;
import dev.samueljunnior.module.message.dto.NewMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerMessageService {

    private final ProducerMessagesService producerMessagesService;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConstants.PRIMARY_QUEUE_NAME, containerFactory = "customerContainerFactory")
    public void receiveMessageFromPrimaryAnalysis(Message message) {
        final var queueName = message.getMessageProperties().getConsumerQueue();
        log.info("Consumindo mensagem da fila: " + queueName);

        final var dto = objectMapper.readValue(message.getBody(), NewMessageDTO.class);

        log.info("Valor: {}", dto.value());
        if(dto.valueIsOdd()){
            log.info("Processamento erro! É um valor IMPAR. Indo para DLQ");
            this.sendDLQ(queueName, message.getMessageProperties(), dto);
            return;
        }

        log.info("Processamende sucesso! É um valor PAR. Produzindo novas mensgens");
        producerMessagesService.sendFanoutMessage(dto);
    }

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConstants.SECONDARY_QUEUE_NAME, containerFactory = "customerContainerFactory")
    public void receiveMessageFromSecondaryAnalysis(Message message) {
        final var queueName = message.getMessageProperties().getConsumerQueue();
        log.info("Consumindo mensagem da fila: " + queueName);

        final var dto = objectMapper.readValue(message.getBody(), NewMessageDTO.class);

        if(dto.value() < 100){
            log.info("Valor menor que 100. Indo para DLQ");
            this.sendDLQ(queueName, message.getMessageProperties(), dto);
            return;
        }

        log.info("Valor maior ou igual a 100. Finalizando processamento com sucesso!");
    }

    @SneakyThrows
    @RabbitListener(queues = RabbitMQConstants.TERTIARY_QUEUE_NAME, containerFactory = "customerContainerFactory")
    public void receiveMessageFromTertiaryAnalysis(Message message) {
        final var queueName = message.getMessageProperties().getConsumerQueue();
        log.info("Consumindo mensagem da fila: " + queueName);

        final var dto = objectMapper.readValue(message.getBody(), NewMessageDTO.class);

        if(dto.value() > 100){
            log.info("Valor maior que 100. Indo para DLQ");
            this.sendDLQ(queueName, message.getMessageProperties(), dto);
            return;
        }

        log.info("Valor menor ou igual a 100. Finalizando processamento com sucesso!");
    }

    @RabbitListener(queues = RabbitMQConstants.DLQ_QUEUE_NAME)
    public void receibedMessageFromDLQ(Message message) throws IOException {
        final var queueName = message.getMessageProperties().getConsumerQueue();
        final var originalQueue = this.getHeaderElement(message.getMessageProperties(), RabbitMQConstants.HEADER_ORIGINAL_QUEUE);
        final var reason = this.getHeaderElement(message.getMessageProperties(), RabbitMQConstants.HEADER_REASON);

        log.info("Processando DLQ: {}", queueName);
        log.info("Fila original: {}", originalQueue);
        log.info("Razão: {}", reason);

        final var dto = objectMapper.readValue(message.getBody(), NewMessageDTO.class);

        if(originalQueue.equals(RabbitMQConstants.PRIMARY_QUEUE_NAME)){
            producerMessagesService.sendDirectMessage(RabbitMQConstants.PRIMARY_QUEUE_NAME, dto.newMessagePlusValue());
            return;
        }

        final var newDto = dto.newMessageDoubledValue();
        if(newDto.value() >= 50){
            log.info("Valor dorbrado {} maior que 50. Reprocessando.", newDto.value());
            producerMessagesService.sendDirectMessage(RabbitMQConstants.PRIMARY_QUEUE_NAME, newDto);
            return;
        }

        log.info("VAlor dobrado {} menor que 50. Finalizando!", newDto.value());
    }

    private String getHeaderElement(MessageProperties messageProperties, String element) {
        if(Objects.isNull(messageProperties.getXDeathHeader())){
            return messageProperties.getHeaders().getOrDefault(element, "").toString();
        }

        final var queue = messageProperties.getXDeathHeader().get(0).get(element);
        return Objects.requireNonNullElse(queue, "").toString();
    }

    private void sendDLQ(String originalQueue, MessageProperties messageProperties, NewMessageDTO dto){
        final var headers = messageProperties.getHeaders();
        headers.put(RabbitMQConstants.HEADER_ORIGINAL_QUEUE, originalQueue);
        producerMessagesService.sendDirectMessageWithHeader(RabbitMQConstants.DLQ_QUEUE_NAME, dto, headers);
    }
}
