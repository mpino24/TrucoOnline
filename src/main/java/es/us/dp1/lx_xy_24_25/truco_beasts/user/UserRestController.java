package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.util.RestPreconditions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.auth.payload.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "UserRestController", description = "La API de gestión de los Usuarios. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
class UserRestController {

	private final UserService userService;
	private final AuthoritiesService authService;

	@Autowired
	public UserRestController(UserService userService, AuthoritiesService authService) {
		this.userService = userService;
		this.authService = authService;
	}

	@Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios. Se puede filtrar por autoridad.", parameters = {
		@Parameter(name = "auth", description = "Nombre de la autenticacion", required = true)
	})
	@ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
	@GetMapping
	public ResponseEntity<List<User>> findAll(@RequestParam(required = false) String auth) {
		List<User> res;
		if (auth != null) {
			res = (List<User>) userService.findAllByAuthority(auth);
		} else
			res = (List<User>) userService.findAll();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "Obtener usuarios paginados", description = "Devuelve una lista paginada de usuarios.")
	@ApiResponse(responseCode = "200", description = "Lista paginada de usuarios obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
	@GetMapping("/paginados")
	public ResponseEntity<Page<User>> findUsuariosPaginados(Pageable pageable) {
		return new ResponseEntity<>(userService.findUsuariosPaginacion(pageable), HttpStatus.OK);
	}

	@Operation(summary = "Obtener todas las autoridades", description = "Devuelve una lista de todas las autoridades.")
	@ApiResponse(responseCode = "200", description = "Lista de autoridades obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Authorities.class)))
	@GetMapping("/authorities")
	public ResponseEntity<List<Authorities>> findAllAuths() {
		List<Authorities> res = (List<Authorities>) authService.findAll();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@Operation(summary = "Obtener usuario por ID", description = "Devuelve un usuario por su ID.")
	@ApiResponse(responseCode = "200", description = "Usuario obtenido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
	@GetMapping(value = "{id}")
	public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
		return new ResponseEntity<>(userService.findUser(id), HttpStatus.OK);
	}

	@Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "Usuario valido a crear.",
			required = true,
			content = @Content(schema = @Schema(implementation = PartidaJugadorView.class))
		))
	@ApiResponse(responseCode = "201", description = "Usuario creado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> create(@RequestBody @Valid User user) {
		User savedUser = userService.saveUser(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar un usuario", description = "Actualiza un usuario existente por su ID.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "Usuario valido a actualizar.",
		required = true,
		content = @Content(schema = @Schema(implementation = PartidaJugadorView.class))
	))
	@ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
	@PutMapping(value = "{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<User> update(@PathVariable("userId") Integer id, @RequestBody User user) {
		RestPreconditions.checkNotNull(userService.findUser(id), "User", "ID", id);
		return new ResponseEntity<>(this.userService.updateUser(user, id), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un usuario", description = "Elimina un usuario por su ID.")
	@ApiResponse(responseCode = "200", description = "Usuario eliminado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
	@DeleteMapping(value = "{userId}")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<MessageResponse> delete(@PathVariable("userId") int id) {
		RestPreconditions.checkNotNull(userService.findUser(id), "User", "ID", id);
		if (userService.findCurrentUser().getId() != id) {
			userService.deleteUser(id);
			return new ResponseEntity<>(new MessageResponse("User deleted!"), HttpStatus.OK);
		} else
			throw new AccessDeniedException("You can't delete yourself!");
	}

}
