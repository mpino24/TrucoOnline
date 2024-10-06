
package es.us.dp1.lx_xy_24_25.your_game_name.partida;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import es.us.dp1.lx_xy_24_25.your_game_name.partida.PartidaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
//apa
@RestController
@RequestMapping("/api/v1/partida")
@Tag(name = "Partida", description = "The Partidas gestion API")
public class PartidaController {
	private final PartidaService partidaService;


	@Autowired
	public PartidaController(PartidaService partidaService) {
		this.partidaService = partidaService;
		}
    
    @GetMapping
	public ResponseEntity<List<Partida>> findAllPartidas() {
		return new ResponseEntity<>(partidaService.findAllPartidasActivas(), HttpStatus.OK);
	}
    @PostMapping
	public ResponseEntity<Partida> createPartida(@RequestBody @Valid Partida Partida) {

		Partida newPartida = new Partida();
		BeanUtils.copyProperties(Partida, newPartida, "id");
		return new ResponseEntity<>(partidaService.savePartida(newPartida), HttpStatus.CREATED);
	}
}