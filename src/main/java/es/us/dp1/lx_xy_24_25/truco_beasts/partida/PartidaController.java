
package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotPartidaFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
//apa
@RestController
@RequestMapping("/api/v1/partida")
@Tag(name = "Partida", description = "The Partidas gestion API")
public class PartidaController {


	private final PartidaService partidaService;
	private final PartidaJugadorService partJugService;

		@Autowired
	public PartidaController(PartidaService partidaService, PartidaJugadorService pjService)   {
		this.partidaService=partidaService;
		this.partJugService=pjService;

	}


	private String generateRandomCode() {
		int length = 5;
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
		Random random = new Random();
		StringBuilder code = new StringBuilder(length);

		for (int i = 0; i < length; i++) {
			code.append(characters.charAt(random.nextInt(characters.length())));
		}
		return code.toString();
	}

	@GetMapping("/partidas")
	public ResponseEntity<List<Partida>> findAll() {
		return new ResponseEntity<>(partidaService.findAllPartidas(), HttpStatus.OK);
	}

   
	@GetMapping("/partidas/accesibles")
	public ResponseEntity<Page<Partida>> findPartidasActivasPublicas(Pageable pageable) {
		return new ResponseEntity<>(partidaService.findAllPartidasActivas(pageable), HttpStatus.OK);
	}

	

	@PostMapping
	public ResponseEntity<Partida> createPartida(@RequestBody @Valid Partida Partida,@RequestParam(required=true) Integer userId) throws AlreadyInGameException {
		
		Partida newPartida = new Partida();
		BeanUtils.copyProperties(Partida, newPartida, "id");
		newPartida.setInstanteInicio(null);
		newPartida.setInstanteFin(null);
		// newPartida.setCodigo(generateRandomCode());
		ResponseEntity<Partida> res=new ResponseEntity<>(partidaService.savePartida(newPartida), HttpStatus.CREATED);
		partJugService.addJugadorPartida(newPartida,userId,true);
		return res;
	}

	@GetMapping(value = "{id}")
	public ResponseEntity<Partida> findPartidaById(@PathVariable("id") int id) {
		return new ResponseEntity<>(partidaService.findPartidaById(id), HttpStatus.OK);
	}

	
	@DeleteMapping("/{codigo}")
	public ResponseEntity<Void> deletePartida(@PathVariable("codigo") String codigo) throws NotPartidaFoundException{
		Partida p= partidaService.findPartidaByCodigo(codigo);
		if(p==null){
			throw new NotPartidaFoundException();
		}
		partidaService.deletePartida(codigo);
		return new ResponseEntity<>(HttpStatus.OK);
	}	

	@GetMapping("/search")
	public ResponseEntity<Partida> findPartidaByCodigo(@RequestParam(required=true) String codigo) {
		return new ResponseEntity<>(partidaService.findPartidaByCodigo(codigo), HttpStatus.OK);
	}

	@PatchMapping("/{codigo}/start")
	public ResponseEntity<String> startGame(@PathVariable("codigo") String codigo){
		partidaService.startGame(codigo);
		return new ResponseEntity<>("Partida comenzada con éxito", HttpStatus.OK);
	}
}