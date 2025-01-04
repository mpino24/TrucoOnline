package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;

@Service
public class EstadisticasService {

    
    private final EstadisticasRepository estadisticasRepository;
    private final JugadorRepository jugadorRepository;
    @Autowired
    public EstadisticasService(EstadisticasRepository estadisticasRepository, JugadorRepository jugadorRepository) {
        this.estadisticasRepository = estadisticasRepository;
        this.jugadorRepository=jugadorRepository;
    }
    @Transactional(readOnly = true)
    public EstadisticaJugador getEstadisticasJugador(Integer jugadorId) throws ResourceNotFoundException{
        Optional<Jugador> j = jugadorRepository.findById(jugadorId);
        if (j.isEmpty()) {
            throw new ResourceNotFoundException("El jugador de ID " + jugadorId + " no fue encontrado");
        }

        EstadisticaJugador res = new EstadisticaJugador();
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugadorId);
        Integer victorias = estadisticasRepository.findVictorias(jugadorId);
        res.setDerrotas(partidasJugadas- victorias);
        res.setPartidasJugadas(partidasJugadas);
        Integer tiempoJugado = estadisticasRepository.findTiempoJugado(jugadorId);
        res.setTiempoJugado(tiempoJugado==null? 0: tiempoJugado);
        res.setVictorias(victorias);
        res.setPartidasA2(estadisticasRepository.findPartidasA2(jugadorId));
        res.setPartidasA4(estadisticasRepository.findPartidasA4(jugadorId));
        res.setPartidasA6(estadisticasRepository.findPartidasA6(jugadorId));
        res.setNumeroFlores(estadisticasRepository.findNumeroFlores(jugadorId));
        res.setQuieros(estadisticasRepository.findQuieros(jugadorId));
        res.setNoQuieros(estadisticasRepository.findNoQuieros(jugadorId));
        res.setNumeroEnganos(estadisticasRepository.findNumeroEnganos(jugadorId));
        res.setPartidasConFlor(estadisticasRepository.findPartidasConFlor(jugadorId));
        return res;
    }

    @Transactional(readOnly = true)
    public EstadisticaGlobal getEstadisticasGlobales() {
        EstadisticaGlobal res = new EstadisticaGlobal();
        
        res.setTiempoJugado(estadisticasRepository.findTiempoJugadoGlobal());
        Integer victorias = estadisticasRepository.findVictoriasGlobal();
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadasGlobal();
        res.setVictorias(victorias);
        res.setDerrotas(partidasJugadas-victorias);
        res.setPartidasJugadas(partidasJugadas);
        res.setPartidasA2(estadisticasRepository.findPartidasA2Global());
        res.setPartidasA4(estadisticasRepository.findPartidasA4Global());
        res.setPartidasA6(estadisticasRepository.findPartidasA6Global());
        res.setNumeroFlores(estadisticasRepository.findNumeroFloresGlobal());
        res.setNumeroEnganos(estadisticasRepository.findNumeroEnganosGlobal());
        res.setQuieros(estadisticasRepository.findQuierosGlobal());
        res.setNoQuieros(estadisticasRepository.findNoQuierosGlobal());
        res.setJugadoresTotales(estadisticasRepository.findJugadoresTotales());
        res.setPartidasConFlor(estadisticasRepository.findPartidasConFlor());
        return res;
        
    }

}
