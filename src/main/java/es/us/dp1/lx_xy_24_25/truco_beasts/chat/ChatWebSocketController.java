package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;

@Controller
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/mensaje")
    public void enviarMensaje(@Payload Mensaje mensaje) throws NotYourChatException {
        chatService.guardarMensaje(mensaje);
        messagingTemplate.convertAndSend("/topic/chat/" + mensaje.getChat().getId(), mensaje);
    }
    
}