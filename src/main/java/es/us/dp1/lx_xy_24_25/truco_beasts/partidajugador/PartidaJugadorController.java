package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/partidajugador")
@Tag(name = "PartidaJugador", description = "The PartidaJugador gestion API")
public class PartidaJugadorController {
    
    private final PartidaJugadorService pjService;
    private final PartidaService partidaService;
    @Autowired
    public PartidaJugadorController(PartidaJugadorService partJugService,PartidaService partidaService)   {
        this.pjService=partJugService;
        this.partidaService=partidaService;

    }

    @GetMapping("/numjugadores")
    public Integer getNumJugadoresPartida(@RequestParam(required=true) Integer partidaId){
        return pjService.getNumJugadoresInPartida(partidaId);
    }

    @PostMapping("/{partidaId}")
    public void addJugadorPartida(@RequestParam(required=true) Integer userId, @PathVariable("partidaId") Integer partidaId) throws AlreadyInGameException{
        Partida partida = partidaService.findPartidaById(partidaId);
        pjService.addJugadorPartida(partida, userId);

    }

    @GetMapping("/connectedTo/{jugadorId}")
    public Integer getNumberOfGamesConnected(@PathVariable("jugadorId") Integer jugadorId){
            return pjService.getNumberOfGamesConnected(jugadorId);
    }
    
}
