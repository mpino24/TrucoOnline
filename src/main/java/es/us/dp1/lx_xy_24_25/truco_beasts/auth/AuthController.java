package es.us.dp1.lx_xy_24_25.truco_beasts.auth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.request.LoginRequest;
import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.request.SignupRequest;
import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.response.JwtResponse;
import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.response.MessageResponse;
import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.jwt.JwtUtils;
import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.services.UserDetailsImpl;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "La API de autenticación basada en JWT (JSON Web Token)")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserService userService;
	private final JwtUtils jwtUtils;
	private final AuthService authService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtils jwtUtils,
			AuthService authService) {
		this.userService = userService;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.authService = authService;
	}

	@Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve un token JWT")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Autenticación exitosa", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
		@ApiResponse(responseCode = "400", description = "Credenciales incorrectas", content = @Content(schema = @Schema(implementation = String.class)))
	})
	@PostMapping("/signin")
	public ResponseEntity authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try{
			Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
			return ResponseEntity.ok().body(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
		}catch(BadCredentialsException exception){
			return ResponseEntity.badRequest().body("Bad Credentials!");
		}
	}

	@Operation(summary = "Validar token", description = "Valida un token JWT")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Token válido", content = @Content(schema = @Schema(implementation = Boolean.class))),
		@ApiResponse(responseCode = "400", description = "Token inválido", content = @Content(schema = @Schema(implementation = Boolean.class)))
	})
	@GetMapping("/validate")
	public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
		Boolean isValid = jwtUtils.validateJwtToken(token);
		return ResponseEntity.ok(isValid);
	}

	@Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
		@ApiResponse(responseCode = "400", description = "Nombre de usuario ya existe", content = @Content(schema = @Schema(implementation = MessageResponse.class)))
	})
	@PostMapping("/signup")	
	public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userService.existsUser(signUpRequest.getUsername()).equals(true)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		authService.createUser(signUpRequest);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

}
