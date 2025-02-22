package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.PartidaNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/partida")
@Tag(name = "Partida", description = "La API de gestión de las partidas. Se debe estar autenticado para utilizarla.")
@SecurityRequirement(name = "bearerAuth")
public class PartidaController {

	private final PartidaService partidaService;
	private final PartidaJugadorService partJugService;

	@Autowired
	public PartidaController(PartidaService partidaService, PartidaJugadorService pjService) {
		this.partidaService = partidaService;
		this.partJugService = pjService;
	}

	@Operation(summary = "Obtener todas las partidas")
	@ApiResponse(responseCode = "200", description = "Lista de partidas obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class)))
	@GetMapping("/partidas")
	public ResponseEntity<List<Partida>> findAll() {
		return new ResponseEntity<>(partidaService.findAllPartidas(), HttpStatus.OK);
	}

	@Operation(summary = "Obtener el historial de partidas del usuario actual")
	@ApiResponse(responseCode = "200", description = "Historial de partidas obtenido con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartidaDTO.class)))
	@GetMapping("/partidas/historial")
	public ResponseEntity<Page<PartidaDTO>> findHistorialPartidas(Pageable pageable) {
		User usuarioActual = partidaService.findUsuarioDelJugadorActual();
		return new ResponseEntity<>(partidaService.findHistorialDePartidas(usuarioActual.getId(), pageable), HttpStatus.OK);
	}

	@Operation(summary = "Obtener partidas activas públicas")
	@ApiResponse(responseCode = "200", description = "Lista de partidas activas públicas obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class)))
	@GetMapping("/partidas/accesibles")
	public ResponseEntity<Page<Partida>> findPartidasActivasPublicas(Pageable pageable) {
		return new ResponseEntity<>(partidaService.findAllPartidasActivas(pageable), HttpStatus.OK);
	}

	@Operation(summary = "Obtener partidas con participantes")
	@ApiResponse(responseCode = "200", description = "Lista de partidas con participantes obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartidaDTO.class)))
	@GetMapping("/partidas/paginadas")
	public ResponseEntity<Page<PartidaDTO>> findPartidasConParticipantes(Pageable pageable) {
		return new ResponseEntity<>(partidaService.findPartidasYParticipantes(pageable), HttpStatus.OK);
	}

	@Operation(summary = "Crear una nueva partida")
	@ApiResponse(responseCode = "201", description = "Partida creada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class)))
	@PostMapping
	public ResponseEntity<Partida> createPartida(@RequestBody @Valid Partida Partida, @RequestParam(required = true) Integer userId) throws AlreadyInGameException {
		PartidaJugador partidaJugador = partJugService.getPartidaJugadorUsuarioActual();
		if (partidaJugador != null) {
			throw new AlreadyInGameException();
		}

		Partida newPartida = new Partida();
		BeanUtils.copyProperties(Partida, newPartida, "id");
		newPartida.setInstanteInicio(null);
		newPartida.setInstanteFin(null);
		ResponseEntity<Partida> res = new ResponseEntity<>(partidaService.savePartida(newPartida), HttpStatus.CREATED);
		partJugService.addJugadorPartida(newPartida, userId, true);
		return res;
	}

	@Operation(summary = "Obtener una partida por ID")
	@ApiResponse(responseCode = "200", description = "Partida obtenida con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class)))
	@GetMapping(value = "{id}")
	public ResponseEntity<Partida> findPartidaById(@PathVariable("id") int id) {
		return new ResponseEntity<>(partidaService.findPartidaById(id), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar una partida por código")
	@ApiResponse(responseCode = "200", description = "Partida eliminada con éxito")
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> deletePartida(@PathVariable("codigo") String codigo) throws PartidaNotFoundException {
		Partida p = partidaService.findPartidaByCodigo(codigo);
		if (p == null) {
			throw new PartidaNotFoundException();
		}
		partidaService.deletePartida(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Operation(summary = "Buscar una partida por código")
	@ApiResponse(responseCode = "200", description = "Partida encontrada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Partida.class)))
	@GetMapping("/search")
	public ResponseEntity<Partida> findPartidaByCodigo(@RequestParam(required = true) String codigo) {
		Partida partida = partidaService.findPartidaByCodigo(codigo);
		if (partida == null) {
			throw new PartidaNotFoundException("La partida con código: " + codigo + " no fue encontrada");
		}
		return new ResponseEntity<>(partida, HttpStatus.OK);
	}

	@Operation(summary = "Iniciar una partida por código")
	@ApiResponse(responseCode = "200", description = "Partida comenzada con éxito")
	@PatchMapping("/{codigo}/start")
	public ResponseEntity<String> startGame(@PathVariable("codigo") String codigo) {
		partidaService.startGame(codigo);
		return new ResponseEntity<>("Partida comenzada con éxito", HttpStatus.OK);
	}

	@ExceptionHandler(AlreadyInGameException.class)
	public ResponseEntity<String> handleAlreadyInGameException(AlreadyInGameException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
	}
}