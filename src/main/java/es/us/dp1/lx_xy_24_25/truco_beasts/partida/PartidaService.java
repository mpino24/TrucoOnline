package es.us.dp1.lx_xy_24_25.truco_beasts.partida;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.chat.Chat;
import es.us.dp1.lx_xy_24_25.truco_beasts.chat.ChatRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.chat.ChatService;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.PartidaNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.PlayerNotConnectedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import jakarta.validation.Valid;

@Service
public class PartidaService {

	private final PartidaRepository partidaRepository;

  	private final UserService userService;

	
	private final PartidaJugadorRepository pjRepository;

	private final ChatRepository chatRepository;

	@Autowired
	public PartidaService(PartidaRepository partidaRepository, UserService userService, PartidaJugadorRepository pjRepository,ChatRepository chatRepository) {
		this.partidaRepository = partidaRepository;
		this.userService = userService;
		this.pjRepository = pjRepository;
		this.chatRepository = chatRepository;
		

  }

	

	public Boolean comprobarFinPartida(Partida partida){
		Boolean res = false;
		Integer puntosEquipo1 = partida.getPuntosEquipo1();
		Integer puntosEquipo2 = partida.getPuntosEquipo2();
		Integer puntosPartida = partida.getPuntosMaximos();

		if (puntosEquipo1==puntosPartida  || puntosEquipo2 == puntosPartida ) {
			res = true;
			partida.setInstanteFin(LocalDateTime.now());
		}
		
		return res;
	}

	@Transactional(readOnly = true)
	public List<Partida> findAllPartidas() throws DataAccessException {
		List<Partida> res = new ArrayList<>();
		partidaRepository.findAll().forEach(x->res.add(x));
		return res;
	}

	@Transactional(readOnly = true)
	public Page<Partida> findAllPartidasActivas(Pageable pageable) throws DataAccessException {
		return partidaRepository.findAllPartidasActivas(pageable);
	}

	@Transactional()
	public Partida savePartida(Partida partida) throws DataAccessException {
		partidaRepository.save(partida);
		return partida;
	}

	@Transactional
	public Partida updatePartida(@Valid Partida partida, Integer idToUpdate) {
		Partida toUpdate = findPartidaById(idToUpdate);
		boolean terminada= partida.haTerminadoLaPartida();
        if(terminada) partida.setInstanteFin(LocalDateTime.now());
		BeanUtils.copyProperties(partida, toUpdate, "id");
		partidaRepository.save(toUpdate);
		return toUpdate;
	}

	@Transactional(readOnly = true)
	public Partida findPartidaById(int id) throws DataAccessException{
		return partidaRepository.findById(id).get();
	}

	@Transactional
	public void deletePartida(String codigo) {
		Partida p= findPartidaByCodigo(codigo);
		partidaRepository.delete(p);
		
	}

	@Transactional(readOnly = true)
	public User findUsuarioDelJugadorActual() {
		return userService.findCurrentUser();
	}

	@Transactional(readOnly = true)
	public Page<PartidaDTO> findPartidasYParticipantes(Pageable pageable) {
		List<Partida> partidas = partidaRepository.findAllPartidas();
        return convertidorDTOs(pageable, partidas, null);
    }
	@Transactional(readOnly = true)
	public Page<PartidaDTO> findHistorialDePartidas(Integer userId, Pageable pageable) {
		List<Partida> partidas = partidaRepository.findAllPartidas();
		String username = userService.findUser(userId).getUsername();
		List<Partida> res = new ArrayList<>();
		for(Partida p:partidas) {
			List<String> nombresJugadoresDeLaPartida = pjRepository.findAllJugadoresPartida(p.getCodigo())
											.stream()
											.map(pj -> pj.getUserName())
											.collect(Collectors.toList());
			if(nombresJugadoresDeLaPartida.contains(username)) res.add(p);
		}
        return convertidorDTOs(pageable, res, username);
    }
	public Page<PartidaDTO> convertidorDTOs(Pageable pageable, List<Partida> partidas, String username) {
		List<PartidaDTO> DTOs = new ArrayList<>();
		for(int i=0;i<partidas.size();i++) {
			Partida partida = partidas.get(i);
			List<PartidaJugadorView> jugadoresDeLaPartida = pjRepository.findAllJugadoresPartida(partida.getCodigo());
			String participantes = jugadoresDeLaPartida
													.stream()
													.map(pj -> pj.getUserName())
													.collect(Collectors.joining(", "));
			PartidaJugador pjCreador = pjRepository.findCreator(partida.getId()).orElse(null);
			String creador = pjCreador == null ? "-" : pjCreador.getPlayer().getUser().getUsername();
			String tipo = partida.getInstanteFin()==null ? "En curso" : "Terminada";
			String visibilidad = partida.getVisibilidad().toString();
			PartidaDTO partidaDTO = new PartidaDTO(partida.getCodigo(), creador, participantes, tipo, visibilidad);
			if(username!=null) {
				partidaDTO.setInicio(partida.getInstanteInicio().toString());
				partidaDTO.setFin(partida.getInstanteFin().toString());
				if(creador==username) partidaDTO.setCreador("SÍ"); else partidaDTO.setCreador("NO");
			}
			DTOs.add(partidaDTO);
		}
		int inicioPagina = (int) pageable.getOffset();
		int finalPagina = Math.min(inicioPagina + pageable.getPageSize(), DTOs.size());
		List<PartidaDTO> DTOsPaginados = DTOs.subList(inicioPagina, finalPagina);
        return new PageImpl<>(DTOsPaginados, pageable, DTOs.size());
	}
	
	@Transactional(readOnly = true)
	public Partida findPartidaByCodigo(String codigo) throws DataAccessException {
		Optional<Partida> p = partidaRepository.findPartidaByCodigo(codigo);
		return p.isEmpty()?null: p.get();
	}

	@Transactional
	public void startGame(String codigo){
		
		Partida partida= findPartidaByCodigo(codigo);
		if(partida==null){
			throw new PartidaNotFoundException();
		}
		
		List<PartidaJugador> jugadores = pjRepository.findPlayersConnectedTo(codigo);
		for(PartidaJugador pj: jugadores){
			if(!pj.getPlayer().getUser().isConnected()){
				throw new PlayerNotConnectedException(pj.getPlayer().getUser().getUsername());
			}
		}

		Integer jugadorMano =  (int) (Math.random() * partida.getNumJugadores());
		partida.setJugadorMano(jugadorMano);
		User currentUser= userService.findCurrentUser();
		User creadorPartida = getGameCreator(partida);
		if(currentUser.getId().equals(creadorPartida.getId())){
			partida.setInstanteInicio(LocalDateTime.now());
			Chat chat= new Chat();
			chat.setPartida(partida);
			//Cambiado al ChatRepository porque se necesita PartidaService en el PartidaJugadorService y sino generaba dependencia circular
			chatRepository.save(chat);
		}else{
			throw new AccessDeniedException();
		}
		
	}

	@Transactional(readOnly = true)
    public User getGameCreator(Partida p) {
        Optional<PartidaJugador> pj = pjRepository.findCreator(p.getId());
        if (pj.isEmpty()) {
            throw new ResourceNotFoundException("Creador de la partida no encontrado");
        }
        return userService.findUser(pj.get().getPlayer().getId());

    }

}
