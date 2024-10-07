package es.us.dp1.lx_xy_24_25.your_game_name.partida;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class PartidaService{


	@Autowired
	 PartidaRepository partidaRepository;

	public List<Partida> findAllPartidasActivas() throws DataAccessException {
		return partidaRepository.findAllPartidasActivas();
	}

	public Partida savePartida(Partida partida) throws DataAccessException {
		partidaRepository.save(partida);
		return partida;
	}
}