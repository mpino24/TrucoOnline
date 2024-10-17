
package es.us.dp1.lx_xy_24_25.your_game_name.partida;

import java.util.List;
import java.util.Random;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
//apa
@RestController
@RequestMapping("/api/v1/partida")
@Tag(name = "Partida", description = "The Partidas gestion API")
public class PartidaController {


	@Autowired	
 	PartidaService partidaService;


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

   
    @GetMapping
	public ResponseEntity<List<Partida>> findAllPartidas() {
		return new ResponseEntity<>(partidaService.findAllPartidasActivas(), HttpStatus.OK);
	}

    @PostMapping
	public ResponseEntity<Partida> createPartida(@RequestBody @Valid Partida Partida) {
		
		Partida newPartida = new Partida();
		BeanUtils.copyProperties(Partida, newPartida, "id");
		newPartida.setCodigo(generateRandomCode());
		return new ResponseEntity<>(partidaService.savePartida(newPartida), HttpStatus.CREATED);
	}

	
	@GetMapping("/{codigo}")
	public ResponseEntity<Partida> findPartidaByCodigo(@PathVariable String codigo) {
		ResponseEntity<Partida> res= new ResponseEntity<>(partidaService.findPartidaByCodigo(codigo), HttpStatus.OK);
		if(res.hasBody()){
			return res;
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
}