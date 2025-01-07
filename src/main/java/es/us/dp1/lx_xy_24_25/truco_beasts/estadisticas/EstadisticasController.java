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

@RestController
@RequestMapping("/api/v1/estadisticas")
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

    @GetMapping("/misEstadisticas")
    public ResponseEntity<EstadisticaJugador> getMisEstadisticas(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return ResponseEntity.ok(estadisticasService.getEstadisticasJugador(jugadorId));
    }
    @GetMapping("/misEstadisticas/datosPorPartida")
    public ResponseEntity<List<DatosPorPartida>> getMisEstadisticasAvanzado(){
        User currentUser= userService.findCurrentUser();
        Integer jugadorId = jugadorService.findJugadorByUserId(currentUser.getId()).getId();
        return ResponseEntity.ok(estadisticasService.getEstadisticasJugadorAvanzadas(jugadorId));
    }

    @GetMapping("/estadisticasGlobales")
    public ResponseEntity<EstadisticaGlobal> getEstadisticasGlobales(){
        return ResponseEntity.ok(estadisticasService.getEstadisticasGlobales());
    }

    @GetMapping("/estadisticasJugador/{jugadorId}")
    public ResponseEntity<EstadisticaJugador> getEstadisticasJugador(@PathVariable("jugadorId") Integer jugadorId) {
        try {
            EstadisticaJugador res = estadisticasService.getEstadisticasJugador(jugadorId);
            return ResponseEntity.ok(res);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    
}
