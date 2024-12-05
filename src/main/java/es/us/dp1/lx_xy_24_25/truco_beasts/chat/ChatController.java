package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/mensaje") // Escucha mensajes enviados a "/app/mensaje" Canal
    public void enviarMensaje(Mensaje mensaje) throws NotYourChatException {
        // Guardar el mensaje en la base de datos
        chatService.guardarMensaje(mensaje);

        String destino= "/topic/chat/"+mensaje.getChat().getId();
        messagingTemplate.convertAndSend(destino,mensaje); //Aquí se envía el mensaje a todos los clientes suscritos al destino

        // Enviar el mensaje al destinatario
        /*messagingTemplate.convertAndSendToUser(
            mensaje.getDestinatario(), // Usuario destinatario
            "/queue/mensajes",         // Cola privada del destinatario
            mensaje                    // Mensaje enviado
        
            );*/
    }

    @GetMapping("/{chatId}")
    public List<Mensaje> getMensajesDe(@PathVariable Integer chatId) throws NotYourChatException{
        return chatService.getMensajesDe(chatId);
    }

    @GetMapping("/lastMessage/{chatId}")
    public Mensaje getLastMessageFrom(@PathVariable Integer chatId) throws NotYourChatException{
        return chatService.getLastMessage(chatId);
    }

}  
