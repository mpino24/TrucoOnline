package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/estadisticas")
@Tag(name = "Estadisticas", description = "La API de gestión de todas las Estadisticas. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;
    private final UserService userService;
    private final JugadorService jugadorService;

    @Autowired
    public EstadisticasController(EstadisticasService estadisticasService, UserService userService, JugadorService jugadorService) {
        this.estadisticasService = estadisticasService;
        this.userService = userService;
        this.jugadorService = jugadorService;
    }

    @Operation(summary = "Obtener mis estadísticas", responses = {
        @ApiResponse(description = "Estadísticas del jugador", responseCode = "200", content = @Content(schema = @Schema(implementation = EstadisticaJugador.class))),
        @ApiResponse(description = "No autorizado", responseCode = "401", content = @Content)
    })
    @GetMapping("/misEstadisticas")
    public ResponseEntity<EstadisticaJugador> getMisEstadisticas(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return ResponseEntity.ok(estadisticasService.getEstadisticasJugador(jugadorId));
    }

    @Operation(summary = "Obtener mis estadísticas avanzadas por partida", responses = {
        @ApiResponse(description = "Estadísticas avanzadas del jugador por partida", responseCode = "200", content = @Content(schema = @Schema(implementation = DatosPorPartida.class))),
        @ApiResponse(description = "No autorizado", responseCode = "401", content = @Content)
    })
    @GetMapping("/misEstadisticas/datosPorPartida")
    public ResponseEntity<List<DatosPorPartida>> getMisEstadisticasAvanzado(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return ResponseEntity.ok(estadisticasService.getEstadisticasJugadorAvanzadas(jugadorId));
    }

    @Operation(summary = "Obtener estadísticas globales", responses = {
        @ApiResponse(description = "Estadísticas globales", responseCode = "200", content = @Content(schema = @Schema(implementation = EstadisticaGlobal.class))),
        @ApiResponse(description = "No autorizado", responseCode = "401", content = @Content)
    })
    @GetMapping("/estadisticasGlobales")
    public ResponseEntity<EstadisticaGlobal> getEstadisticasGlobales(){
        return ResponseEntity.ok(estadisticasService.getEstadisticasGlobales());
    }

    @Operation(summary = "Obtener estadísticas de un jugador por ID", responses = {
        @ApiResponse(description = "Estadísticas del jugador", responseCode = "200", content = @Content(schema = @Schema(implementation = EstadisticaJugador.class))),
        @ApiResponse(description = "Jugador no encontrado", responseCode = "404", content = @Content),
        @ApiResponse(description = "No autorizado", responseCode = "401", content = @Content)
    })
    @GetMapping("/estadisticasJugador/{jugadorId}")
    public ResponseEntity<EstadisticaJugador> getEstadisticasJugador(@PathVariable("jugadorId") Integer jugadorId) {
        try {
            EstadisticaJugador res = estadisticasService.getEstadisticasJugador(jugadorId);
            return ResponseEntity.ok(res);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Operation(summary = "Obtener ranking global de jugadores", responses = {
        @ApiResponse(description = "Ranking global de jugadores", responseCode = "200", content = @Content(schema = @Schema(implementation = JugadorVictorias.class))),
        @ApiResponse(description = "No autorizado", responseCode = "401", content = @Content)
    })
    @GetMapping("/ranking")
    public ResponseEntity<List<JugadorVictorias>> getRankingGlobal(){
        return ResponseEntity.ok(estadisticasService.getRankingGlobal(null));
    }
}
