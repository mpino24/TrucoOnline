
package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/jugador")
@Tag(name = "Jugador", description = "The Players gestion API")
public class JugadorController {

    @Autowired
    JugadorService jugadorService;


    @GetMapping
	public ResponseEntity<Jugador> findJugadorByUserId(@RequestParam(required=true) String userId) {
		return new ResponseEntity<>(jugadorService.findJugadorByUserId(Integer.valueOf(userId)), HttpStatus.OK);
	}

    @GetMapping("/amigos")
    public ResponseEntity<Jugador> findAmigosByUserId(@RequestParam(required=true) String userId) {
		return new ResponseEntity<>(jugadorService.findAmigosByUserId(Integer.valueOf(userId)), HttpStatus.OK);
	}
    
}