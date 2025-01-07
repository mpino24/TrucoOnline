package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import jakarta.validation.Valid;



@Controller
public class PartidaJugadorWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public PartidaJugadorWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/partjugador")
    public void enviarPartidaJugador(@Payload @Valid PartidaJugadorView partJug){
        messagingTemplate.convertAndSend("/topic/partida/" + partJug.getPartidaId(), partJug);
    }
    
}
