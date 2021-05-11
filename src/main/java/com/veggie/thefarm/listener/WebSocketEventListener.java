package com.veggie.thefarm.listener;

import com.veggie.thefarm.model.dto.InviteMessage;
import com.veggie.thefarm.model.dto.MessageType;
import com.veggie.thefarm.utils.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Arrays;
import java.util.Objects;

@Component
public class WebSocketEventListener extends LoggerUtils {

    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("New web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        System.out.println(headerAccessor);

        if (username != null) {
            logger.info("User Disconnected : " + username);

            InviteMessage inviteMessage = new InviteMessage();
            inviteMessage.setType(MessageType.LEAVE);
            inviteMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/public", inviteMessage);
        }
    }

}
