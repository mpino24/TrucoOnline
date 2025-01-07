package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;




import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;



@RestController
@RequestMapping("/api/v1/manos/{codigo}")
public class ManoController {


    private final ManoService manoService;

    @Autowired
    public ManoController(ManoService manoService){
        this.manoService = manoService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mano> actualizarMano(@PathVariable String codigo){
        Mano mano = manoService.getMano(codigo);
        return new ResponseEntity<>(mano, HttpStatus.OK);
    }
    @PatchMapping("/tirarCarta/{cartaId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Carta> patchTirarCarta(@PathVariable String codigo, @PathVariable Integer cartaId) throws CartaTiradaException{
        Carta carta = manoService.tirarCarta(codigo, cartaId);
        return new ResponseEntity<>(carta, HttpStatus.OK);
    }

    @PatchMapping("/cantarTruco/{cantoTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarTruco(@PathVariable String codigo, @PathVariable Cantos cantoTruco) throws TrucoException {
        
        manoService.cantosTruco(codigo, cantoTruco);
        return new ResponseEntity<>(cantoTruco, HttpStatus.OK);
    }
    
    @PatchMapping("/responderTruco/{respuestasTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Integer> patchResponderTruco(@PathVariable String codigo, @PathVariable Cantos respuestasTruco) throws TrucoException {
        
        manoService.responderTruco(codigo, respuestasTruco);
        Integer puntosActualesTruco = manoService.getMano(codigo).getPuntosTruco();
        return new ResponseEntity<>(puntosActualesTruco, HttpStatus.OK);
    }

    @PatchMapping("/cantarEnvido/{cantoEnvido}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarEnvido(@PathVariable String codigo, @PathVariable Cantos cantoEnvido) throws EnvidoException {
        manoService.cantosEnvido(codigo, cantoEnvido);
        return new ResponseEntity<>(cantoEnvido, HttpStatus.OK);
    }

    @PatchMapping("/responderEnvido/{cantoEnvido}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchResponderEnvido(@PathVariable String codigo, @PathVariable Cantos cantoEnvido) throws EnvidoException {
        manoService.responderEnvido(codigo, cantoEnvido);
        return new ResponseEntity<>(cantoEnvido, HttpStatus.OK);
    }
    @PatchMapping("/cantarFlor/{cantoFlor}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchCantarFlor(@PathVariable String codigo, @PathVariable Cantos cantoFlor) throws FlorException {
        manoService.cantosFlor(codigo, cantoFlor);
        return new ResponseEntity<>(cantoFlor, HttpStatus.OK);
    }

    @PatchMapping("/responderFlor/{cantoFlor}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Cantos> patchResponderFlor(@PathVariable String codigo, @PathVariable Cantos cantoFlor) throws FlorException {
        manoService.responderFlor(codigo, cantoFlor);
        return new ResponseEntity<>(cantoFlor, HttpStatus.OK);
    }
}