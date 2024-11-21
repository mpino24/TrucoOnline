package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;



@RestController
@RequestMapping("/api/v1/mano")
public class ManoController {


    private final ManoService manoService;

    @Autowired
    public ManoController(ManoService manoService){
        this.manoService = manoService;
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Mano> actualizarMano(@RequestParam(required=true) String codigo){
        Mano mano = manoService.getMano(codigo);
        return new ResponseEntity<>(mano, HttpStatus.OK);
    }


    @GetMapping("/idJugadorPie")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorPie(){
        return manoService.obtenerJugadorPie();

    }

    @GetMapping("/idJugadorAnterior/{idJugadorActual}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorAnterior(@PathVariable("idJugadorActual") Integer idJugadorActual){
        return manoService.obtenerJugadorAnterior(idJugadorActual);

    }

    @GetMapping("/idJugadorSiguiente/{idJugadorActual}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorSiguiente(@PathVariable("idJugadorActual") Integer idJugadorActual){
        return manoService.siguienteJugador(idJugadorActual);

    }

    @GetMapping("/jugadorGanadorRonda")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorGanadorRonda(){
        return manoService.compararCartas();
    }

    @PatchMapping("/tirarCarta/{idCarta}")
    @ResponseStatus(HttpStatus.OK)
    public void getTirarCarta(@PathVariable("idCarta") Integer idCarta){
        manoService.tirarCarta(idCarta);
    }

    @PatchMapping("/cantarTruco/{canto}")
    @ResponseStatus(HttpStatus.OK)
    public void realizarCantoTruco(@PathVariable("canto") CantosTruco canto) throws Exception{
        manoService.cantosTruco(canto);
    }

    @PatchMapping("/respuestaTruco/{respuesta}")
    @ResponseStatus(HttpStatus.OK)
    public void responderAlTruco(@PathVariable("respuesta") RespuestasTruco respuesta) throws Exception{
        manoService.responderTruco(respuesta);
    }
    
}