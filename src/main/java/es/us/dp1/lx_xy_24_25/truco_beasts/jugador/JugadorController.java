
package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jugador")
@Tag(name = "Jugador", description = "The Players gestion API")
public class JugadorController {

    @Autowired
    JugadorService jugadorService;


    @GetMapping
	public ResponseEntity<JugadorDTO> findJugadorByUserId(@RequestParam(required=true) String userId) {
		return new ResponseEntity<>(jugadorService.findJugadorByUserId(Integer.valueOf(userId)), HttpStatus.OK);
	}

    @GetMapping("/amigos")
    public ResponseEntity<List<JugadorDTO>> findAmigosByUserId(@RequestParam(required=true) String userId) {
		return new ResponseEntity<>(jugadorService.findAmigosByUserId(Integer.valueOf(userId)), HttpStatus.OK);
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
}