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
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Service
public class PartidaJugadorService {

    	PartidaJugadorRepository pjRepository;
        JugadorRepository jugRepository;
        UserService userService;
        PartidaRepository partidaRepository;

	@Autowired
	public PartidaJugadorService(PartidaJugadorRepository partJugRepository, JugadorRepository jugadorRepo,UserService userService,PartidaRepository partidaRepository) {
		this.pjRepository = partJugRepository;
        this.jugRepository= jugadorRepo;
        this.userService= userService;
        this.partidaRepository= partidaRepository;
	}

    @Transactional(readOnly = true)
    public Integer getNumJugadoresInPartida(Integer partidaId){
        return pjRepository.findNumJugadoresPartida(partidaId);
    }

    @Transactional(readOnly = true)
    public Integer getNumberOfGamesConnected(Integer jugadorId){
        return pjRepository.numberOfGamesConnected(jugadorId);
    }
    
    @Transactional(readOnly = true)
    public Integer getMiPosicion(Integer userId, Integer partidaId) throws ResourceNotFoundException{
        Partida partida = partidaRepository.findById(partidaId).get();
        PartidaJugador partjugador = pjRepository.findPlayersConnectedTo(partida.getCodigo()).stream().filter(pj-> pj.getPlayer().getId().equals(userId)).findFirst().orElse(null);
        if (partjugador == null) {
            throw new ResourceNotFoundException("No se encontro la partidaJugador pedida");
        }
        return partjugador.getPosicion();
    }

    @Transactional
    public void addJugadorPartida(Partida partida, Integer userId, Boolean isCreator) throws AlreadyInGameException{
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
            partJug.setIsCreator(isCreator);
            pjRepository.save(partJug);
        }

    }


    @Transactional
    public void eliminateJugadorPartida(Integer userId){
        Partida partida = getPartidaOfUserId(userId);
        List<PartidaJugador> jugadores= pjRepository.findPlayersConnectedTo(partida.getCodigo());
        Integer creadorId = jugadores.stream().filter(pj->pj.getIsCreator()).map(pj->pj.getPlayer().getId()).findFirst().orElse(null);
        if(creadorId!=null && creadorId.equals(userId)){
            PartidaJugador nuevoCreador = jugadores.stream().filter(pj->!pj.getPlayer().getId().equals(userId)).findFirst().orElse(null);
            pjRepository.deleteByPlayerId(userId);
            if(nuevoCreador!=null){
                nuevoCreador.setIsCreator(true);
                pjRepository.save(nuevoCreador);
             }else{
                partidaRepository.delete(partida);
            }
        }
        
    }

    @Transactional(readOnly=true)
    public List<PartidaJugadorDTO> getPlayersConnectedTo(String partidaCode){ 
        return pjRepository.findPlayersConnectedTo(partidaCode).stream().map(pj-> new PartidaJugadorDTO(pj)).toList();
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

    @Transactional(readOnly=true)
    public User getGameCreator(Partida p){
        Optional<PartidaJugador> pj = pjRepository.findCreator(p.getId());
        if(pj.isEmpty()){
            throw new ResourceNotFoundException("Creador de la partida no encontrado");
        }
        return userService.findUser(pj.get().getPlayer().getId());

    }
    
}
