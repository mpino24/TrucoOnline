package es.us.dp1.lx_xy_24_25.truco_beasts.partida;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartidaService {

    PartidaRepository partidaRepository;

	@Autowired
	public PartidaService(PartidaRepository partidaRepository) {
		this.partidaRepository = partidaRepository;
	}

    @Transactional(readOnly = true)
    public Partida findPartidaByCodigo(String codigo) throws DataAccessException {
        return partidaRepository.findPartidaByCodigo(codigo);
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
}
