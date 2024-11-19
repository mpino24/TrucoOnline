package es.us.dp1.lx_xy_24_25.truco_beasts.partida;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Service
public class PartidaService {

    PartidaRepository partidaRepository;

	ManoService manoService;
	UserService userService;
	PartidaJugadorService partidaJugadorService;

	@Autowired
	public PartidaService(PartidaRepository partidaRepository, ManoService manoService, UserService userService, PartidaJugadorService partidaJugadorService) {
		this.partidaRepository = partidaRepository;
		this.manoService = manoService;
		this.userService = userService;
		this.partidaJugadorService = partidaJugadorService;
	}


	//PROVISIONAL
	public Mano crearMano(Partida partida){
		Mano mano = new Mano();
		mano.setPartida(partida);
		mano.setJugadorTurno(partida.getJugadorMano());
		return mano;
	}
	//PROVISIONAL
	public void terminarMano(Mano mano, Partida partida){
		Integer ganador = mano.getGanadoresRondas().get(mano.getGanadoresRondas().size()-1);
		partida.setJugadorMano(manoService.siguienteJugador(partida.getJugadorMano()));
		
	}

	@Transactional(readOnly = true)
	public List<Partida> findAllPartidas() throws DataAccessException {
		List<Partida> res = new ArrayList<>();
		partidaRepository.findAll().forEach(x->res.add(x));
		return res;
	}

	@Transactional(readOnly = true)
	public List<Partida> findAllPartidasActivas() throws DataAccessException {
		return partidaRepository.findAllPartidasActivas();
	}

	@Transactional()
	public Partida savePartida(Partida partida) throws DataAccessException {
		partidaRepository.save(partida);
		return partida;
	}

	@Transactional(readOnly = true)
	public Partida findPartidaById(int id) throws DataAccessException{
		return partidaRepository.findById(id).get();
	}

	@Transactional()
	public void deletePartida(String codigo) {
		Partida p= findPartidaByCodigo(codigo);
		partidaRepository.delete(p);
		
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
			throw new ResourceNotFoundException("La partida no existe");
		}
		User currentUser= userService.findCurrentUser();
		User creadorPartida = partidaJugadorService.getGameCreator(partida);
		if(currentUser.getId().equals(creadorPartida.getId())){
			partida.setInstanteInicio(LocalDateTime.now());
		}else{
			throw new AccessDeniedException();
		}
		
	}

}
