package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/mensaje") // Escucha mensajes enviados a "/app/mensaje"
    public void enviarMensaje(Mensaje mensaje) {
        // Guardar el mensaje en la base de datos
        chatService.guardarMensaje(mensaje);

        // Enviar el mensaje al destinatario
        /*messagingTemplate.convertAndSendToUser(
            mensaje.getDestinatario(), // Usuario destinatario
            "/queue/mensajes",         // Cola privada del destinatario
            mensaje                    // Mensaje enviado
        );*/
    }
}
