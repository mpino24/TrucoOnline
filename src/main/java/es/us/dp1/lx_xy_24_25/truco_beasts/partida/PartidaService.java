package es.us.dp1.lx_xy_24_25.truco_beasts.partida;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;

@Service
public class PartidaService {

    PartidaRepository partidaRepository;

	ManoService manoService;

	@Autowired
	public PartidaService(PartidaRepository partidaRepository, ManoService manoService) {
		this.partidaRepository = partidaRepository;
		this.manoService = manoService;
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

}
