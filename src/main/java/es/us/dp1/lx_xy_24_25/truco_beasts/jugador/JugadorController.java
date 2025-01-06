
package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jugador")
@Tag(name = "Jugador", description = "The Players gestion API")
public class JugadorController {

    @Autowired
    JugadorService jugadorService;
    @Autowired
    UserService userService;


    @GetMapping
    public ResponseEntity<JugadorDTO> findJugadorByUserId(@RequestParam(required=true) String userId) {
        return new ResponseEntity<>(jugadorService.findJugadorByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @GetMapping("/edit/{userId}")
    public ResponseEntity<JugadorDTO> findJugadorByUserIdEdit(@PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(jugadorService.findJugadorByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }

    @PutMapping("/edit/{userId}")
    public ResponseEntity<Jugador> updateJugadorByUserIdEdit( @PathVariable("userId") Integer userId, 
    @RequestBody @Valid Jugador jugador) {
        Jugador res = jugadorService.updateJugador(jugador, userService.findUser(userId));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Jugador> saveJugadorEdit(@RequestBody @Valid Jugador jugador, @RequestParam Integer userId) {
        User user = userService.findUser(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario no encontrado");
        }
        jugador.setUser(user);
        Jugador savedJugador = jugadorService.saveJugador(jugador);
        return new ResponseEntity<>(savedJugador, HttpStatus.OK);
    }

    @GetMapping("/amigos")
    public ResponseEntity<List<JugadorDTO>> findAmigosByUserId(@RequestParam(required=true) String userId) {
        return new ResponseEntity<>(jugadorService.findAmigosByUserId(Integer.valueOf(userId)), HttpStatus.OK);
    }
    @GetMapping("/solicitudes")
    public ResponseEntity<List<JugadorDTO>> findSolicitudesByUserId() {
        User currentUser= userService.findCurrentUser();
        return new ResponseEntity<>(jugadorService.findSolicitudesByUserId(currentUser.getId()), HttpStatus.OK);
    }

    @GetMapping("/{userName}")
    public ResponseEntity<JugadorDTO> findJugadorByJugadorName(@PathVariable String userName){
        JugadorDTO j= jugadorService.findJugadorByUserName(userName);
        if(j!=null){
            return new ResponseEntity<>(jugadorService.findJugadorByUserName(userName),HttpStatus.OK);
        }else{
            throw new ResourceNotFoundException("Jugador no encontrado");
        }
    }
    
    @PostMapping
    public ResponseEntity<Jugador> saveJugador(@RequestBody @Valid Jugador jugador, @Valid User user) {
        return null;
    }

    @GetMapping("/{userId}/isFriend/{friendUserName}")
    public ResponseEntity<Boolean> checkIfAreFriends(@PathVariable int userId, @PathVariable String friendUserName){
        return new ResponseEntity<>(jugadorService.checkIfAreFriends(friendUserName, userId),HttpStatus.OK);
    }

    @PatchMapping("/isFriend/{amigoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity addNewFriend(@PathVariable int amigoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.addNewFriends(currentUser.getId(), amigoId);
        return new ResponseEntity<>(void.class,HttpStatus.OK);
    }
   
    @DeleteMapping("isFriend/{amigoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteFriend(@PathVariable int amigoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.deleteFriends(currentUser.getId(), amigoId);
        return new ResponseEntity<>(void.class,HttpStatus.OK);
    }
    
    @PatchMapping("{userId}/isSolicitado/{solicitadoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity crearSolicitud(@PathVariable int userId, @PathVariable int solicitadoId){
        jugadorService.crearSolicitud(userId, solicitadoId);
        return new ResponseEntity<>(void.class,HttpStatus.OK);
    }


    @DeleteMapping("/isSolicitado/{solicitadoId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteSolicitud(@PathVariable int solicitadoId){
        User currentUser= userService.findCurrentUser();
        jugadorService.deleteSolicitud(currentUser.getId(), solicitadoId);
        return new ResponseEntity<>(void.class,HttpStatus.OK);
    }
}