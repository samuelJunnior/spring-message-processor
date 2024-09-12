package dev.samueljunnior.module.message.dto;

import java.io.Serializable;

public record NewMessageDTO(String transactionalId, Integer value) implements Serializable {

    public NewMessageDTO newMessagePlusValue(){
        return new NewMessageDTO(this.transactionalId, this.value + 1);
    }

    public NewMessageDTO newMessageDoubledValue(){
        return new NewMessageDTO(this.transactionalId, this.value * 2);
    }

    public boolean valueIsOdd(){
        return this.value % 2 != 0;
    }
}
