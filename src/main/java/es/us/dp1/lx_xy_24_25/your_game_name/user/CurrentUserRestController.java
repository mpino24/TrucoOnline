package es.us.dp1.lx_xy_24_25.your_game_name.user;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.your_game_name.configuration.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/v1/profile")
public class CurrentUserRestController {
    
    private final UserService userService;

	@Autowired
	public CurrentUserRestController(UserService userService) {
		this.userService = userService;
	}

    @GetMapping
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        UsernamePasswordAuthenticationToken principalToken;
        UserDetailsImpl user;
        User res;
        if(principal != null) {
            principalToken = (UsernamePasswordAuthenticationToken) principal;
            user = (UserDetailsImpl) principalToken.getPrincipal();
            res = userService.findUser(user.getId());
        } else {
            res = null;
        }
        return new ResponseEntity<User>(res, HttpStatus.OK);
    }
}
