package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

@Service
public class PartidaJugadorService {

    	PartidaJugadorRepository pjRepository;
        JugadorRepository jugRepository;

	@Autowired
	public PartidaJugadorService(PartidaJugadorRepository partJugRepository, JugadorRepository jugadorRepo) {
		this.pjRepository = partJugRepository;
        this.jugRepository= jugadorRepo;
	}

    @Transactional(readOnly = true)
    public Integer getNumJugadoresInPartida(Integer partidaId){
        return pjRepository.findNumJugadoresPartida(partidaId);
    }

    @Transactional
    public void addJugadorPartida(Partida partida, Integer userId){
        PartidaJugador partJug = new PartidaJugador();
        partJug.setGame(partida);
        Optional<Jugador> jugadorOpt = jugRepository.findByUserId(userId);
        if(jugadorOpt.isEmpty()){
            throw new ResourceNotFoundException("Jugador no encontrado");
        }else{
            partJug.setPlayer(jugadorOpt.get());
            partJug.setPosicion(1); //POR AHORA
            partJug.setEquipo(Equipo.EQUIPO1); //POR AHORA
            pjRepository.save(partJug);
        }

    }
    
}
