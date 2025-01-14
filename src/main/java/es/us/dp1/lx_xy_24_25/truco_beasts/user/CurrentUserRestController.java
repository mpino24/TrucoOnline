package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.response.MessageResponse;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.PerfilJugadorUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "CurrentUser", description = "La API de gestión del Usuario Actual. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class CurrentUserRestController {
    
    private final UserService userService;
    private final JugadorService jugadorService;

    private final static String relog = "RELOG";
    private final static String home = "HOME";

    @Autowired
    public CurrentUserRestController(UserService userService, JugadorService jugadorService) {
        this.userService = userService;
        this.jugadorService = jugadorService;
    }

    @Operation(summary = "Obtener el perfil del usuario actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido con éxito", content = @Content(schema = @Schema(implementation = PerfilJugadorUsuario.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<PerfilJugadorUsuario> getProfile(Principal principal) {
        User user = userService.findCurrentUser();
        JugadorDTO jugadorDto = jugadorService.findJugadorDTOByUserId(user.getId());
        Jugador jugador = jugadorService.findJugadorById(jugadorDto.getId());
        PerfilJugadorUsuario perfil = new PerfilJugadorUsuario();
        perfil.setJugador(jugador);
        perfil.setUser(user);
        
        return ResponseEntity.ok(perfil);
        }

        @Operation(summary = "Actualizar el perfil del usuario actual", requestBody = @RequestBody(description = "Perfil del usuario a actualizar", 
        required = true, content = @Content(schema = @Schema(implementation = PerfilJugadorUsuario.class))))
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado con éxito", content = @Content(schema = 
        @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
        })
        @PutMapping("/edit")
        public ResponseEntity<?> updateProfile(
         @Valid @org.springframework.web.bind.annotation.RequestBody PerfilJugadorUsuario perfil, 
        Principal principal) {
        User user = perfil.getUser();
        Jugador jugador = perfil.getJugador();
        
        User currentUser = userService.findCurrentUser();
        
        Boolean mismoUsername = user.getUsername().equals(currentUser.getUsername());
        Boolean mismaContraseña = (user.getPassword() == null || user.getPassword().isEmpty());
        
        userService.updateCurrentUser(user);
        jugadorService.updateJugador(jugador, currentUser);

        if (!mismoUsername || !mismaContraseña) {    
            return ResponseEntity.ok(relog);
        } else {
            return ResponseEntity.ok(home);
        }
        }

        @Operation(summary = "Borrar la cuenta del usuario actual")
        @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta borrada con éxito", content = @Content(schema = 
        @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
        })
        @DeleteMapping("/borrarMiCuenta")
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<MessageResponse> borrarMiCuenta() {
        Integer userId = userService.findCurrentUser().getId();
        jugadorService.deleteJugadorByUserId(userId);
        return ResponseEntity.ok(new MessageResponse("¡Tu cuenta fue borrada con éxito!"));
    }

    @Operation(summary = "Actualizar la conexión del usuario actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conexión actualizada con éxito", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @PatchMapping("/updateConnection")
    public ResponseEntity<MessageResponse> updateConnection() {
        userService.updateConnection();
        return ResponseEntity.ok(new MessageResponse("¡Conexión actualizada!"));
    }
}