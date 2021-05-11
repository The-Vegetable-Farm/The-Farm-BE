package com.veggie.thefarm.controller;

import com.veggie.thefarm.model.dto.InviteMessage;
import com.veggie.thefarm.utils.LoggerUtils;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

/**
 * '/app' 으로 시작하는 대상이 있는 클라이언트에서 보낸 모든 메시지는 @MessageMapping 메소드로 라우팅 됨
 */
@RestController
public class MiniGameController extends LoggerUtils {

    /**
     * 미니게임 초대
     */
    @MessageMapping("/game.invite")
    @SendTo("/topic/public")
    public InviteMessage sendMessage(@Payload InviteMessage inviteMessage) {
        return inviteMessage;
    }

    /**
     * 미니게임 입장 또는 퇴장
     */
    @MessageMapping("/game.access")
    @SendTo("/topic/public")
    public InviteMessage addUser(@Payload InviteMessage inviteMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", inviteMessage.getSender());
        return inviteMessage;
    }

}
