package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@Controller
public class PartidaJugadorWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    public PartidaJugadorWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(
        summary = "Enviar partida jugador",
        description = "Envía información de la partida del jugador a un tópico específico",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Mensaje enviado exitosamente",
                content = @Content(schema = @Schema(implementation = PartidaJugadorView.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Solicitud inválida",
                content = @Content
            )
        }
    )
    @MessageMapping("/partjugador")
    public void enviarPartidaJugador(
        @RequestBody(
            description = "Información de la partida del jugador",
            required = true,
            content = @Content(schema = @Schema(implementation = PartidaJugadorView.class))
        )
        @Payload @Valid PartidaJugadorView partJug
    ) {
        messagingTemplate.convertAndSend("/topic/partida/" + partJug.getPartidaId(), partJug);
    }
}
