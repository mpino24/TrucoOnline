package es.us.dp1.lx_xy_24_25.truco_beasts.auth;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.AuthoritiesService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class AuthService {

	private final PasswordEncoder encoder;
	private final AuthoritiesService authoritiesService;
	private final UserService userService;
	private final JugadorService jugadorService;
	

	@Autowired
	public AuthService(PasswordEncoder encoder, AuthoritiesService authoritiesService, UserService userService,
			JugadorService jugadorService
			) {
		this.encoder = encoder;
		this.authoritiesService = authoritiesService;
		this.userService = userService;
		this.jugadorService = jugadorService;
	}

	@Transactional
	public void createUser(@Valid SignupRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setLastConnection(LocalDateTime.now());
		user.setPassword(encoder.encode(request.getPassword()));
		String strRoles = request.getAuthority();
		Authorities role;
		String fotoDefault = "http://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/perfiles/robot.jpg";
		switch (strRoles.toLowerCase()) {
		case "admin":
			role = authoritiesService.findByAuthority("ADMIN");
			user.setAuthority(role);
			userService.saveUser(user);
			break;
		default:
			role = authoritiesService.findByAuthority("PLAYER");
			user.setAuthority(role);
			userService.saveUser(user);
		}
		String foto = request.getPhoto();
		Jugador jugador = new Jugador();
		jugador.setFirstName(request.getFirstName());
		jugador.setLastName(request.getLastName());
		if(request.getEmail()!=null && !request.getEmail().trim().isEmpty()) jugador.setEmail(request.getEmail());
		if (foto!= null && !foto.trim().isEmpty()) jugador.setPhoto(foto);
		else jugador.setPhoto(fotoDefault);
		jugador.setUser(user);

		jugadorService.saveJugador(jugador);
	}

}
