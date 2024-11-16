package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/mano")
public class ManoController {



    @GetMapping("/idJugadorPie")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorPie(){
        return ManoService.obtenerJugadorPie();

    }

    @GetMapping("/idJugadorAnterior/{idJugadorActual}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorAnterior(@PathVariable("idJugadorActual") Integer idJugadorActual){
        return ManoService.obtenerJugadorAnterior(idJugadorActual);

    }

    @GetMapping("/idJugadorSiguiente/{idJugadorActual}")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorSiguiente(@PathVariable("idJugadorActual") Integer idJugadorActual){
        return ManoService.siguienteJugador(idJugadorActual);

    }

    @GetMapping("/jugadorGanadorRonda")
    @ResponseStatus(HttpStatus.OK)
    public Integer getJugadorGanadorRonda(){
        return ManoService.compararCartas();
    }

    @PatchMapping("/tirarCarta/{idCarta}")
    @ResponseStatus(HttpStatus.OK)
    public void getTirarCarta(@PathVariable("idCarta") Integer idCarta){
        ManoService.tirarCarta(idCarta);
    }

    @PatchMapping("/cantarTruco/{canto}")
    @ResponseStatus(HttpStatus.OK)
    public void realizarCantoTruco(@PathVariable("canto") CantosTruco canto) throws Exception{
        ManoService.cantosTruco(canto);
    }

    @PatchMapping("/respuestaTruco/{respuesta}")
    @ResponseStatus(HttpStatus.OK)
    public void responderAlTruco(@PathVariable("respuesta") RespuestasTruco respuesta) throws Exception{
        ManoService.responderTruco(respuesta);
    }
    
}
