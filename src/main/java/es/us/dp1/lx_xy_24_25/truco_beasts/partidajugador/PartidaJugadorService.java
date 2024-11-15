package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;
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

    @Transactional(readOnly = true)
    public Integer getNumberOfGamesConnected(Integer jugadorId){
        return pjRepository.numberOfGamesConnected(jugadorId);
    }


    @Transactional
    public void addJugadorPartida(Partida partida, Integer userId) throws AlreadyInGameException{
        PartidaJugador partJug = new PartidaJugador();
        partJug.setGame(partida);
        Optional<Jugador> jugadorOpt = jugRepository.findByUserId(userId);
        if(jugadorOpt.isEmpty()){
            throw new ResourceNotFoundException("Jugador no encontrado");
        }else{
            if(pjRepository.numberOfGamesConnected(jugadorOpt.get().getId())>0){
                throw new AlreadyInGameException("Ya estás conectado a una partida");
            }
            
            partJug.setPlayer(jugadorOpt.get());
            List<Integer> posiciones =pjRepository.lastPosition(partida.getId());
            Integer posi= posiciones.stream().max(Comparator.naturalOrder()).orElse(-1);
            partJug.setPosicion(posi+1); 
            pjRepository.save(partJug);
        }

    }

    @Transactional
    public void eliminateJugadorPartida(Integer userId){
        pjRepository.deleteByPlayerId(userId);
    }

    @Transactional(readOnly=true)
    public List<PartidaJugador> getPlayersConnectedTo(String partidaCode){
        return pjRepository.findPlayersConnectedTo(partidaCode);
    }

    @Transactional(readOnly=true)
    public Partida getPartidaOfUserId(Integer userId){
        Optional<Partida> res= pjRepository.findPartidaByUserId(userId);
        if(res.isEmpty()){
            throw new ResourceNotFoundException("El usuario no está en ninguna partida.");
        }else{
            return res.get();
        }
    }

    @Transactional
    public void changeTeamOfUser(Integer userId) throws TeamIsFullException{
        Partida partida = getPartidaOfUserId(userId);
        List<Integer> posiciones =pjRepository.lastPosition(partida.getId());
        PartidaJugador partjugador = pjRepository.findPlayersConnectedTo(partida.getCodigo()).stream().filter(pj-> pj.getPlayer().getId().equals(userId)).findFirst().orElse(null);
        if(partjugador==null){
            throw new ResourceNotFoundException("El usuario no pertenece a la partida");
        }
        Integer posInicial = partjugador.getPosicion();
        Integer posNueva = IntStream.range(0, partida.getNumJugadores()).boxed().filter(p->!posiciones.contains(p)).filter(p->posInicial%2!=p%2).findFirst().orElse(null);
        if(posNueva==null){
            throw new TeamIsFullException();
        }
        partjugador.setPosicion(posNueva);
        pjRepository.save(partjugador);
    }
    
}
