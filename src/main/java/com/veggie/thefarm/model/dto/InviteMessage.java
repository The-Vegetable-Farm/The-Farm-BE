package com.veggie.thefarm.model.dto;

import lombok.Data;

@Data
public class InviteMessage {

    private MessageType type;
    private String content;
    private String sender;

}
