package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1/profile")
public class CurrentUserRestController {
    
    private final UserService userService;
    private final PasswordEncoder encoder;

	@Autowired
	public CurrentUserRestController(UserService userService, PasswordEncoder encoder) {
		this.userService = userService;
        this.encoder = encoder;
	}

    @GetMapping
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userService.findCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateProfile(@RequestBody User user, Principal principal) {
        User currentUser = userService.findCurrentUser();
        if(currentUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
        currentUser.setUsername(user.getUsername());
        if(!(user.getPassword()==null)) {
            currentUser.setPassword(encoder.encode(user.getPassword()));
        }
        userService.saveUser(currentUser);

        return ResponseEntity.ok("Perfil editado con exito");
    }
}
