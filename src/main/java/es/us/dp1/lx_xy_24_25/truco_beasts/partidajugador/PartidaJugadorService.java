package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Estado;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Service
public class PartidaJugadorService {

    private final PartidaJugadorRepository pjRepository;
    private final JugadorRepository jugRepository;
    private final UserService userService;

    private final PartidaService partidaService;
  

   
    public PartidaJugadorService(PartidaJugadorRepository partJugRepository, JugadorRepository jugadorRepo, UserService userService, PartidaService partidaService) {
        this.pjRepository = partJugRepository;
        this.jugRepository = jugadorRepo;
        this.userService = userService;
        
        this.partidaService = partidaService;
    }


    @Transactional(readOnly = true)
    public Integer getNumJugadoresInPartida(Integer partidaId) {
        return pjRepository.findNumJugadoresPartida(partidaId);
    }

    @Transactional(readOnly = true)
    public Integer getNumberOfGamesConnected(Integer jugadorId) {
        return pjRepository.numberOfGamesConnected(jugadorId);
    }

    @Transactional(readOnly = true)
    public Integer getMiPosicion(Integer userId, Integer partidaId) throws ResourceNotFoundException {
        Partida partida = partidaService.findPartidaById(partidaId);
        PartidaJugador partjugador = pjRepository.findPlayersConnectedTo(partida.getCodigo()).stream().filter(pj -> pj.getPlayer().getUser().getId().equals(userId)).findFirst().orElse(null);
        if (partjugador == null) {
            throw new ResourceNotFoundException("No se encontro la partidaJugador pedida");
        }
        return partjugador.getPosicion();
    }

    @Transactional(readOnly = true)
    public List<Integer> getJugadoresInPartida(String codigoPartida) {
        return pjRepository.findPlayersConnectedTo(codigoPartida).stream().map(pj -> pj.getPlayer().getId()).toList();
    }

    @Transactional
    public void addJugadorPartida(Partida partida, Integer userId, Boolean isCreator) throws AlreadyInGameException {
        PartidaJugador partJug = new PartidaJugador();
        partJug.setGame(partida);
        Optional<Jugador> jugadorOpt = jugRepository.findByUserId(userId);
        if (jugadorOpt.isEmpty()) {
            throw new ResourceNotFoundException("Jugador no encontrado");
        } else {
            if (pjRepository.numberOfGamesConnected(jugadorOpt.get().getId()) > 0) {
                throw new AlreadyInGameException("Ya estás conectado a una partida");
            }

            partJug.setPlayer(jugadorOpt.get());
            List<Integer> todasPosiciones = IntStream.range(0, partida.getNumJugadores()).boxed().toList();
            List<Integer> posicionesOcupadas = pjRepository.lastPosition(partida.getId());

            List<Integer> posDisponibles = todasPosiciones.stream().filter(p -> !posicionesOcupadas.contains(p)).toList();

            Integer posi = posDisponibles.stream().min(Comparator.naturalOrder()).get();
            partJug.setPosicion(posi);
            partJug.setIsCreator(isCreator);
            pjRepository.save(partJug);
        }

    }

    @Transactional(rollbackFor = { NotAuthorizedException.class, ResourceNotFoundException.class })
    public void eliminateJugadorPartida(Integer partidaJugadorId) throws ResourceNotFoundException, NotAuthorizedException {
        User currentUser = userService.findCurrentUser();
        PartidaJugador parJug = pjRepository.findById(partidaJugadorId).orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el jugador con id " + partidaJugadorId));
        Integer jugadorExpulsadoId = parJug.getPlayer().getId();
        Partida partidaActual = parJug.getGame();
        List<PartidaJugador> listaPartidaJugadores = pjRepository.findPlayersConnectedTo(partidaActual.getCodigo());
        Integer creadorId = listaPartidaJugadores.stream()
                .filter(pj -> pj.getIsCreator())
                .map(pj -> pj.getPlayer().getId())
                .findFirst()
                .orElse(null);
        
        
        Integer jugadorActualId = jugRepository.findByUserId(currentUser.getId()).orElseThrow(()->
            new ResourceNotFoundException("No se ha encontrado el jugador actual")).getId();

        if(jugadorExpulsadoId == null){
            jugadorExpulsadoId=jugadorActualId;
        }


        if (jugadorExpulsadoId != null && jugadorExpulsadoId !=jugadorActualId) {
            if (jugadorActualId ==creadorId) {
                pjRepository.deleteById(partidaJugadorId);
            } else {
                throw new NotAuthorizedException("No tienes permiso para eliminar a jugadores de la partida");
            }
        } else { 
            
            
            pjRepository.deleteById(partidaJugadorId);
            
            //necesario ya que en java las variables de lambda deben ser final o efectivamente final
            final Integer finalJugadorExpulsadoId = jugadorExpulsadoId;
            PartidaJugador nuevoCreador = listaPartidaJugadores.stream()
                    .filter(pj -> pj.getPlayer().getId()  != finalJugadorExpulsadoId)
                    .findFirst()
                    .orElse(null);

            if (creadorId != null && creadorId == jugadorActualId && nuevoCreador != null) {
                nuevoCreador.setIsCreator(true);
                pjRepository.save(nuevoCreador);
            } else if (nuevoCreador == null) {
                partidaService.deletePartida(partidaActual.getCodigo()); 
                
            }
            
        }

        if (partidaActual.getEstado().equals(Estado.ACTIVE)) {
            checkEndGame(parJug);
            
        }
        
            
        
    }

    public void checkEndGame(PartidaJugador partidaJugador) {
        Partida partida = partidaJugador.getGame();
        Equipo equipoDelQueHuyo = partidaJugador.getEquipo();
        Integer puntos = partida.getPuntosMaximos();
        
        if(equipoDelQueHuyo.equals(Equipo.EQUIPO1)){
            partida.setPuntosEquipo2(puntos);
          
        }else{
            partida.setPuntosEquipo1(puntos);
        }
        partidaService.updatePartida(partida, partida.getId());
    }


    @Transactional(readOnly = true)
    public List<PartidaJugadorDTO> getPlayersConnectedTo(String partidaCode) {
        return pjRepository.findPlayersConnectedTo(partidaCode).stream().map(pj -> new PartidaJugadorDTO(pj)).toList();
    }

    @Transactional(readOnly = true)
    public Partida getPartidaOfUserId(Integer userId) {
        Optional<Partida> res = pjRepository.findPartidaByUserId(userId);
        if (res.isEmpty()) {
            throw new ResourceNotFoundException("El usuario no está en ninguna partida.");
        } else {
            
            return res.get();
        }
    }

    @Transactional(readOnly = true)
    public PartidaJugador getPartidaJugadorUsuarioActual() {
        PartidaJugador res = null;
        User user = userService.findCurrentUser();
        if(user != null){
        Integer userId = user.getId();
        
        Integer jugadorId = jugRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el jugador actual")).getId();
        res = pjRepository.findByPlayerIdAndGameNotFinish(jugadorId);
        }
        if(res == null || res.getGame().getEstado() == Estado.FINISHED){
            res=null;
        }
        
            
        return res;
        
    }

    @Transactional
    public void changeTeamOfUser(Integer userId) throws TeamIsFullException {
        Partida partida = getPartidaOfUserId(userId);
        List<Integer> posiciones = pjRepository.lastPosition(partida.getId());
        PartidaJugador partjugador = pjRepository.findPlayersConnectedTo(partida.getCodigo()).stream().filter(pj -> pj.getPlayer().getId().equals(userId)).findFirst().orElse(null);
        if (partjugador == null) {
            throw new ResourceNotFoundException("El usuario no pertenece a la partida");
        }
        Integer posInicial = partjugador.getPosicion();
        Integer posNueva = IntStream.range(0, partida.getNumJugadores()).boxed().filter(p -> !posiciones.contains(p)).filter(p -> posInicial % 2 != p % 2).findFirst().orElse(null);
        if (posNueva == null) {
            throw new TeamIsFullException();
        }
        partjugador.setPosicion(posNueva);
        pjRepository.save(partjugador);
    }

    @Transactional(readOnly = true)
    public User getGameCreator(Partida p) {
        Optional<PartidaJugador> pj = pjRepository.findCreator(p.getId());
        if (pj.isEmpty()) {
            throw new ResourceNotFoundException("Creador de la partida no encontrado");
        }
        return userService.findUser(pj.get().getPlayer().getId());

    }

    public List<PartidaJugadorView> getAllJugadoresPartida(String codigo){
        Optional<Partida> partida= pjRepository.findPartidaByCodigoPartida(codigo);
        if(partida.isEmpty()){
            throw new ResourceNotFoundException("No se ha encontrado Partida con codigo '"+ codigo + "'");
        }
        return pjRepository.findAllJugadoresPartida(codigo);
    }


    public void eliminateJugadorPartidaByJugadorId(Integer jugadorId) {
        PartidaJugador pj = pjRepository.findByPlayerIdAndGameNotFinish(jugadorId);
        if(pj==null){
            throw new ResourceNotFoundException("No se ha encontrado el jugador con id "+ jugadorId);
        }
        eliminateJugadorPartida(pj.getId());
    }


}
