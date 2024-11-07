package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.security.Principal;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.PerfilJugadorUsuario;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class CurrentUserRestController {
    
    private final UserService userService;
    private final JugadorService jugadorService;
    private final static String relog = "RELOG";
    private final static String home = "HOME";

	@Autowired
	public CurrentUserRestController(UserService userService, JugadorService jugadorService)   {
		this.userService = userService;
        this.jugadorService=jugadorService;

	}

    @GetMapping
    public ResponseEntity<PerfilJugadorUsuario> getProfile(Principal principal) {
        User user = userService.findCurrentUser();
        JugadorDTO jugadorDto = jugadorService.findJugadorByUserId(user.getId());
        Jugador jugador = jugadorService.findJugadorById(jugadorDto.getId());
        PerfilJugadorUsuario perfil = new PerfilJugadorUsuario();
        perfil.setJugador(jugador);
        perfil.setUser(user);
        
        return ResponseEntity.ok(perfil);
    }

    
    @PutMapping("/edit")

public ResponseEntity<?> updateProfile(@RequestBody @Valid PerfilJugadorUsuario perfil, Principal principal) {
    User user = perfil.getUser();
    Jugador jugador = perfil.getJugador();
    
    User currentUser = userService.findCurrentUser();
    
    Boolean mismoUsername = user.getUsername().equals(currentUser.getUsername());
    Boolean mismaContraseña = (user.getPassword()==null || user.getPassword().isEmpty());
    
    jugadorService.updateJugador(jugador, currentUser);
    userService.updateCurrentUser(user);

    if(!mismoUsername || !mismaContraseña) {    
        return ResponseEntity.ok(relog);
    } else {
        return ResponseEntity.ok(home);
    }
}

    


}
