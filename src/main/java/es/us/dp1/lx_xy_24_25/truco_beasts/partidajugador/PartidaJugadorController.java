package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/partidajugador")
@Tag(name = "PartidaJugador", description = "The PartidaJugador gestion API")
public class PartidaJugadorController {
    
    private final PartidaJugadorService pjService;
    private final PartidaService partidaService;
    private final UserService userService;
    @Autowired

    public PartidaJugadorController(PartidaJugadorService partJugService,PartidaService partidaService,UserService userService)   {
        this.pjService=partJugService;
        this.partidaService=partidaService;
        this.userService=userService;
    }

    @GetMapping("/miposicion/{partidaId}")
    public Integer getMiPosicion(@PathVariable("partidaId") Integer partidaId){
        Integer userId = userService.findCurrentUser().getId();
        return pjService.getMiPosicion(userId, partidaId);
    }

    @GetMapping("/numjugadores")
    public Integer getNumJugadoresPartida(@RequestParam(required=true) Integer partidaId){
        return pjService.getNumJugadoresInPartida(partidaId);
    }

    @PostMapping("/{partidaId}")
    public void addJugadorPartida(@PathVariable("partidaId") Integer partidaId) throws AlreadyInGameException{
        User currentUser= userService.findCurrentUser();
        Partida partida = partidaService.findPartidaById(partidaId);
        pjService.addJugadorPartida(partida, currentUser.getId(),false);
    }

    @GetMapping("/connectedTo/{jugadorId}")
    public Integer getNumberOfGamesConnected(@PathVariable("jugadorId") Integer jugadorId){
            return pjService.getNumberOfGamesConnected(jugadorId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void eliminateJugadorPartida(){
        User currentUser = userService.findCurrentUser();
        pjService.eliminateJugadorPartida(currentUser.getId());
    }

    @GetMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    public List<PartidaJugadorDTO> getPlayersConnectedTo(@RequestParam(required=true) String partidaCode){
        return pjService.getPlayersConnectedTo(partidaCode);
    }

    @PatchMapping("/changeteam")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> changeTeamOfUser(@RequestParam(required=true) Integer userId) throws TeamIsFullException{
        pjService.changeTeamOfUser(userId);
        return new ResponseEntity<>("Usuario cambiado de equipo",HttpStatus.OK);
    }
    
}
