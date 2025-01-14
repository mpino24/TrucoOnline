package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/partidajugador")
@Tag(name = "PartidaJugador", description = "La API de gestión de los PartidaJugador. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class PartidaJugadorController {
    
    private final PartidaJugadorService pjService;
    private final PartidaService partidaService;
    private final UserService userService;
    
    @Autowired
    public PartidaJugadorController(PartidaJugadorService pjService, PartidaService partidaService, UserService userService) {
        this.pjService = pjService;
        this.partidaService = partidaService;
        this.userService = userService;
    }

    @Operation(summary = "Obtener mi posición en la partida", responses = {
        @ApiResponse(responseCode = "200", description = "Posición obtenida", content = @Content(schema = @Schema(implementation = Integer.class))),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada", content = @Content)
    })
    @GetMapping("/miposicion/{partidaId}/{userId}")
    public Integer getMiPosicion(@PathVariable("partidaId") Integer partidaId,@PathVariable("userId") Integer userId) throws ResourceNotFoundException {
        return pjService.getMiPosicion(userId, partidaId);
    }

    @Operation(summary = "Obtener número de jugadores en la partida", responses = {
        @ApiResponse(responseCode = "200", description = "Número de jugadores obtenido", content = @Content(schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/numjugadores")
    public Integer getNumJugadoresPartida(@RequestParam(required = true) Integer partidaId) {
        return pjService.getNumJugadoresInPartida(partidaId);
    }

    @Operation(summary = "Añadir jugador a la partida", responses = {
        @ApiResponse(responseCode = "200", description = "Jugador añadido"),
        @ApiResponse(responseCode = "409", description = "Jugador ya está en la partida", content = @Content)
    })
    @PostMapping("/{partidaId}")
    public void addJugadorPartida(@PathVariable("partidaId") Integer partidaId) throws AlreadyInGameException {
        User currentUser = userService.findCurrentUser();
        Partida partida = partidaService.findPartidaById(partidaId);
        pjService.addJugadorPartida(partida, currentUser.getId(), false);
    }

    @Operation(summary = "Obtener número de partidas conectadas por jugador", responses = {
        @ApiResponse(responseCode = "200", description = "Número de partidas obtenido", content = @Content(schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/connectedTo/{jugadorId}")
    public Integer getNumberOfGamesConnected(@PathVariable("jugadorId") Integer jugadorId) {
        return pjService.getNumberOfGamesConnected(jugadorId);
    }

    @Operation(summary = "Obtener partida jugador actual", responses = {
        @ApiResponse(responseCode = "200", description = "Partida jugador actual obtenida", content = @Content(schema = @Schema(implementation = PartidaJugadorDTO.class))),
        @ApiResponse(responseCode = "202", description = "No hay partida jugador actual", content = @Content)
    })
    @GetMapping("/partidaJugadorActual")
    public ResponseEntity<PartidaJugadorDTO> getPartidaJugadorActual() {
        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();
        if (pj == null) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
        }
        pj.getPlayer().setAmigos(null);
        pj.getPlayer().setSolicitudes(null);
        return ResponseEntity.ok(new PartidaJugadorDTO(pj));
    }

    @Operation(summary = "Eliminar jugador de la partida", responses = {
        @ApiResponse(responseCode = "200", description = "Jugador eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content)
    })
    @DeleteMapping("/salir")
    public ResponseEntity<String> eliminateJugadorPartida() {
        try {
            PartidaJugador partidaJugador = pjService.getPartidaJugadorUsuarioActual();
            pjService.eliminateJugadorPartida(partidaJugador.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente");
        } catch (NotAuthorizedException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }

    @Operation(summary = "Eliminar jugador de la partida por ID", responses = {
        @ApiResponse(responseCode = "200", description = "Jugador eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "403", description = "No autorizado", content = @Content)
    })
    @DeleteMapping("/eliminarJugador/{jugadorId}")
    public ResponseEntity<String> eliminateJugadorPartidaByJugadorId(@PathVariable("jugadorId") Integer jugadorId) {
        try {
            pjService.eliminateJugadorPartidaByJugadorId(jugadorId);
            return ResponseEntity.status(HttpStatus.OK).body("Se elimino correctamente");
        } catch (NotAuthorizedException exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }

    @Operation(summary = "Obtener jugadores conectados a una partida", responses = {
        @ApiResponse(responseCode = "200", description = "Jugadores obtenidos", content = @Content(schema = @Schema(implementation = PartidaJugadorDTO.class)))
    })
    @GetMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    public List<PartidaJugadorDTO> getPlayersConnectedTo(@RequestParam(required = true) String partidaCode) {
        return pjService.getPlayersConnectedTo(partidaCode);
    }

    @Operation(summary = "Cambiar equipo de usuario", responses = {
        @ApiResponse(responseCode = "200", description = "Usuario cambiado de equipo", content = @Content),
        @ApiResponse(responseCode = "400", description = "Equipo lleno", content = @Content)
    })
    @PatchMapping("/changeteam")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> changeTeamOfUser(@RequestParam(required = true) Integer userId) throws TeamIsFullException {
        try {
            pjService.changeTeamOfUser(userId);
            return new ResponseEntity<>("Usuario cambiado de equipo", HttpStatus.OK);
        } catch (TeamIsFullException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @Operation(summary = "Obtener todos los jugadores de una partida por código", responses = {
        @ApiResponse(responseCode = "200", description = "Jugadores obtenidos", content = @Content(schema = @Schema(implementation = PartidaJugadorView.class))),
        @ApiResponse(responseCode = "404", description = "Partida no encontrada", content = @Content)
    })
    @GetMapping("/jugadores/codigoPartida/{codigo}")
    @ResponseStatus(HttpStatus.OK)
    public List<PartidaJugadorView> getAllJugadoresPartida(@PathVariable("codigo") String codigo) throws ResourceNotFoundException {
        return pjService.getAllJugadoresPartida(codigo);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(name + " parameter is missing");
    }
}
