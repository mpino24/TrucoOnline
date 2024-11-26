package es.us.dp1.lx_xy_24_25.truco_beasts.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carta")
public class CartaController {
    CartaService cartaService;
    
    @Autowired
    public CartaController(CartaService cartaService){
        this.cartaService = cartaService;
    }
    
    @GetMapping("/id/{idCarta}")
    	public ResponseEntity<Carta> findCartaById(@PathVariable("idCarta") Integer idCarta) {
		return new ResponseEntity<>(cartaService.findCartaById(idCarta), HttpStatus.OK);
	}
}
