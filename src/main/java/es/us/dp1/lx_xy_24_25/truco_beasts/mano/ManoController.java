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

import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;
import io.micrometer.core.ipc.http.HttpSender.Response;



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
    public ResponseEntity<Carta> patchTirarCarta(@PathVariable String codigo, @PathVariable Integer cartaId){
        Mano mano = manoService.getMano(codigo);
        Carta carta = mano.tirarCarta(cartaId);
        manoService.actualizarMano(mano, codigo);
        return new ResponseEntity<>(carta, HttpStatus.OK);
    }

    @PatchMapping("/cantarTruco/{cantoTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CantosTruco> patchCantarTruco(@PathVariable String codigo, @PathVariable CantosTruco cantoTruco) throws Exception {
        Mano mano = manoService.getMano(codigo);
        mano.cantosTruco(cantoTruco);
        manoService.actualizarMano(mano, codigo);
        return new ResponseEntity<>(cantoTruco, HttpStatus.OK);
    }
    
    @PatchMapping("/responderTruco/{respuestasTruco}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RespuestasTruco> patchResponderTruco(@PathVariable String codigo, @PathVariable RespuestasTruco respuestasTruco) throws Exception {
        Mano mano = manoService.getMano(codigo);
        mano.responderTruco(respuestasTruco);
        manoService.actualizarMano(mano, codigo);
        return new ResponseEntity<>(respuestasTruco, HttpStatus.OK);
    }
}