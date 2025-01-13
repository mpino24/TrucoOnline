package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.request.LoginRequest;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jugador")
@Tag(name = "Jugador", description = "La API de gestión del jugador. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class JugadorController {

    @Autowired
    JugadorService jugadorService;
    @Autowired
    UserService userService;

    @Operation(summary = "Buscar jugador por ID de usuario", parameters = {
        @Parameter(name = "userId", description = "ID del usuario", required = true)
    })
    @ApiResponse(responseCode = "200", description = "Jugador encontrado", content = @Content(schema = @Schema(implementation = JugadorDTO.class)))
    @GetMapping
    public ResponseEntity<JugadorDTO> findJugadorByUserId(@RequestParam(required=true) String userId) {
        return new ResponseEntity<>(jugadorService.findJugadorDTOByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @Operation(summary = "Buscar jugador por ID de usuario para editar")
    @ApiResponse(responseCode = "200", description = "Jugador encontrado", content = @Content(schema = @Schema(implementation = JugadorDTO.class)))
    @GetMapping("/edit/{userId}")
    public ResponseEntity<JugadorDTO> findJugadorByUserIdEdit(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(jugadorService.findJugadorDTOByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @Operation(summary = "Actualizar jugador por ID de usuario",requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Jugador.class)
        )
    ))
    @ApiResponse(responseCode = "200", description = "Jugador actualizado", content = @Content(schema = @Schema(implementation = Jugador.class)))
    @PutMapping("/edit/{userId}")
    public ResponseEntity<Jugador> updateJugadorByUserIdEdit(@PathVariable("userId") Integer userId, 
    @RequestBody @Valid Jugador jugador) {
        Jugador res = jugadorService.updateJugador(jugador, userService.findUser(userId));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Buscar amigos por ID de usuario", parameters = {
        @Parameter(name = "userId", description = "ID del usuario", required = true)
    })
    @ApiResponse(responseCode = "200", description = "Amigos encontrados", content = @Content(schema = @Schema(implementation = JugadorDTO.class)))
    @GetMapping("/amigos")
    public ResponseEntity<List<JugadorDTO>> findAmigosByUserId(@RequestParam(required=true) String userId) {
        return new ResponseEntity<>(jugadorService.findAmigosByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @Operation(summary = "Buscar solicitudes por ID de usuario")
    @ApiResponse(responseCode = "200", description = "Solicitudes encontradas", content = @Content(schema = @Schema(implementation = JugadorDTO.class)))
    @GetMapping("/solicitudes")
    public ResponseEntity<List<JugadorDTO>> findSolicitudesByUserId() {
        User currentUser= userService.findCurrentUser();
        return new ResponseEntity<>(jugadorService.findSolicitudesByUserId(currentUser.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Buscar jugador por nombre de usuario")
    @ApiResponse(responseCode = "200", description = "Jugador encontrado", content = @Content(schema = @Schema(implementation = JugadorDTO.class)))
    @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    @GetMapping("/{userName}")
    public ResponseEntity<JugadorDTO> findJugadorByJugadorName(@PathVariable String userName){
        JugadorDTO j= jugadorService.findJugadorByUserName(userName);
        if(j!=null){
            return new ResponseEntity<>(jugadorService.findJugadorByUserName(userName),HttpStatus.OK);
        }else{
            throw new ResourceNotFoundException("Jugador no encontrado");
        }
    }

    @Operation(summary = "Eliminar jugador por ID de usuario")
    @ApiResponse(responseCode = "200", description = "Jugador eliminado")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteJugadorWithUserId(@PathVariable("userId") Integer userId) {
        jugadorService.deleteJugadorByUserId(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Agregar nuevo amigo")
    @ApiResponse(responseCode = "200", description = "Amigo agregado")
    @PatchMapping("/isFriend/{amigoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> addNewFriend(@PathVariable int amigoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.addNewFriends(currentUser.getId(), amigoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Eliminar amigo")
    @ApiResponse(responseCode = "200", description = "Amigo eliminado")
    @DeleteMapping("/isFriend/{amigoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteFriend(@PathVariable int amigoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.deleteFriends(currentUser.getId(), amigoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Crear solicitud")
    @ApiResponse(responseCode = "200", description = "Solicitud creada")
    @PatchMapping("{userId}/isSolicitado/{solicitadoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> crearSolicitud(@PathVariable int userId, @PathVariable int solicitadoId){
        jugadorService.crearSolicitud(userId, solicitadoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Eliminar solicitud")
    @ApiResponse(responseCode = "200", description = "Solicitud eliminada")
    @DeleteMapping("/isSolicitado/{solicitadoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteSolicitud(@PathVariable int solicitadoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.deleteSolicitud(currentUser.getId(), solicitadoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}