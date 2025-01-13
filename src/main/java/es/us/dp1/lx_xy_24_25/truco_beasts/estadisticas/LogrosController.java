package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/logros")
@Tag(name = "Logros", description = "La API de gestión de los Logros. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class LogrosController {

    private final LogrosService logrosService;
    private final UserService userService;
    private final JugadorService jugadorService;

    @Autowired
    public LogrosController(LogrosService logrosService, UserService userService, JugadorService jugadorService){
        this.logrosService= logrosService;
        this.userService=userService;
        this.jugadorService=jugadorService;
    }

    @Operation(summary = "Obtener todos los logros", responses = {
        @ApiResponse(description = "Lista de logros", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class)))
    })
    @GetMapping
    public ResponseEntity<List<Logros>> getTodosLosLogros(){
        Boolean esAdmin = false;
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        if (currentUser.hasAuthority("ADMIN")) {
            esAdmin=true;
        }
        return new ResponseEntity<>(logrosService.findAllLogros(esAdmin,jugadorId), HttpStatus.OK);
    }

    @Operation(summary = "Obtener el total de logros", responses = {
        @ApiResponse(description = "Total de logros", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class)))
    })
    @GetMapping("/total")
    public ResponseEntity<Integer> getTotalLogros(){
        return new ResponseEntity<>(logrosService.findTotalLogros(), HttpStatus.OK);
    }

    @Operation(summary = "Obtener mis logros", responses = {
        @ApiResponse(description = "Lista de mis logros", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class)))
    })
    @GetMapping("/misLogros")
    public ResponseEntity<List<Logros>> getMisLogros(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return new ResponseEntity<>(logrosService.logrosConseguidos(jugadorId), HttpStatus.OK);
    }

    @Operation(summary = "Crear un nuevo logro", responses = {
        @ApiResponse(description = "Logro creado", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class))),
        @ApiResponse(description = "No autorizado", responseCode = "403", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Logros> createLogro( @Valid @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class))) Logros logro){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            Logros newLogro = new Logros();
            BeanUtils.copyProperties(logro, newLogro, "id");
            return new ResponseEntity<>(logrosService.save(newLogro), HttpStatus.CREATED);
        }else{
            throw new NotAuthorizedException();
        }
    }

    @Operation(summary = "Eliminar un logro", responses = {
        @ApiResponse(description = "Logro eliminado", responseCode = "200", content = @Content),
        @ApiResponse(description = "No autorizado", responseCode = "403", content = @Content),
        @ApiResponse(description = "Logro no encontrado", responseCode = "404", content = @Content)
    })
    @DeleteMapping("/{logroId}")
    public ResponseEntity<Void> deleteLogro(@PathVariable("logroId") Integer logroId){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            if(logrosService.findLogroById(logroId)==null){
                throw new ResourceNotFoundException("No se encontro el logro");
            }
            logrosService.deleteLogro(logroId);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            throw new NotAuthorizedException();
        }
    }

    @Operation(summary = "Actualizar un logro", responses = {
        @ApiResponse(description = "Logro actualizado", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class))),
        @ApiResponse(description = "No autorizado", responseCode = "403", content = @Content)
    })
    @PutMapping("/{logroId}")
    public ResponseEntity<Logros> updateLogro(@Valid @RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = Logros.class))) Logros logro, @PathVariable("logroId") Integer logroId){
        User currentUser = userService.findCurrentUser();
        if(currentUser.hasAuthority("ADMIN")){
            return new ResponseEntity<>(logrosService.updateLogro(logro, logroId), HttpStatus.CREATED);
        }else{
            throw new NotAuthorizedException();
        }
    }
}
