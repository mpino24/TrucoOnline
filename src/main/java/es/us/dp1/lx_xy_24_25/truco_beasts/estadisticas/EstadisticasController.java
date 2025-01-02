package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@RestController
@RequestMapping("/api/v1/estadisticas")
public class EstadisticasController {


    private final EstadisticasService estadisticasService;

    private final UserService userService;

    @Autowired
    public EstadisticasController(EstadisticasService estadisticasService, UserService userService) {
        this.estadisticasService = estadisticasService;
        this.userService = userService;

    }

    @RequestMapping("/misEstadisticas")
    public ResponseEntity<EstadisticaJugador> getMisEstadisticas(){
        User currentUser= userService.findCurrentUser();
        return ResponseEntity.ok(estadisticasService.getEstadisticasJugador(currentUser.getId()));
    }
    
}
