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
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.PerfilJugadorUsuario;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class CurrentUserRestController {
    
    private final UserService userService;
    private final JugadorService jugadorService;

	@Autowired
	public CurrentUserRestController(UserService userService, JugadorService jugadorService)   {
		this.userService = userService;
        this.jugadorService=jugadorService;

	}

    @GetMapping
    public ResponseEntity<PerfilJugadorUsuario> getProfile(Principal principal) {
        User user = userService.findCurrentUser();
        Jugador jugador = jugadorService.findJugadorByUserId(user.getId());
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
  
     if(!mismoUsername || !mismaContraseña) {
        userService.updateCurrentUser(user);
        return ResponseEntity.ok("Perfil editado con exito");
    } else {
        jugadorService.updateJugador(jugador, currentUser);
        return ResponseEntity.ok("Nada cambiado");
    }
}

    


}
