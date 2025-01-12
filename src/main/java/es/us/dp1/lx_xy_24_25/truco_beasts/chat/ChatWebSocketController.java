package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "Chat WebSocket Controller", description = "Controlador para manejar los mensajes de chat a través de WebSocket")
public class ChatWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/mensaje")
    @Operation(summary = "Enviar mensaje", description = "Envía un mensaje a un chat específico")
    public void enviarMensaje(@Payload Mensaje mensaje) throws NotYourChatException {
        try {
            MensajeDTO mensajeDto = chatService.guardarMensaje(mensaje);
            messagingTemplate.convertAndSend("/topic/chat/" + mensaje.getChat().getId(), mensajeDto);
        } catch (NotYourChatException e) {
            mensaje.setContenido("ERROR: " + e.getMessage() + "\n" + "Te han eliminado de amigos.");
            messagingTemplate.convertAndSend("/topic/chat/" + mensaje.getChat().getId(), new MensajeDTO(mensaje));
        }
    }

    @MessageMapping("/mensajepartida")
    @Operation(summary = "Enviar mensaje de partida", description = "Envía un mensaje a un chat de partida específico")
    public void enviarMensajePartida(@Payload Mensaje mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());
        MensajeDTO mensajeDto = new MensajeDTO(mensaje);
        messagingTemplate.convertAndSend("/topic/gamechat/" + mensaje.getChat().getPartida().getCodigo(), mensajeDto);
    }
}
