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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NameDuplicatedException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/profile")
public class CurrentUserRestController {
    
    private final UserService userService;
    

	@Autowired
	public CurrentUserRestController(UserService userService) {
		this.userService = userService;

	}

    @GetMapping
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userService.findCurrentUser();
        return ResponseEntity.ok(user);
    }

    @PutMapping("/edit")
    public ResponseEntity<?> updateProfile(@RequestBody @Valid User user, Principal principal) {
        
        User cambiado = userService.updateCurrentUser(user);
        if(cambiado.getUsername().equals(user.getUsername()) && user.getPassword()==null){
            return ResponseEntity.ok("Nada cambiado");
        }else{
            return ResponseEntity.ok("Perfil editado con exito");
        }
        
    }
}
