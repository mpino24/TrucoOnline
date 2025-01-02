package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

    
    private final EstadisticasRepository estadisticasRepository;

    @Autowired
    public EstadisticasService(EstadisticasRepository estadisticasRepository) {
        this.estadisticasRepository = estadisticasRepository;
    }

    public EstadisticaJugador getEstadisticasJugador(Integer jugadorId){
        EstadisticaJugador res = new EstadisticaJugador();
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugadorId);
        Integer victorias = estadisticasRepository.findVictorias(jugadorId);
        res.setDerrotas(partidasJugadas- victorias);
        res.setPartidasJugadas(partidasJugadas);
        Integer tiempoJugado = estadisticasRepository.findTiempoJugado(jugadorId);
        res.setTiempoJugado(tiempoJugado==null? Duration.ofSeconds(0): Duration.ofSeconds(tiempoJugado));
        res.setVictorias(victorias);
        res.setPartidasA2(estadisticasRepository.findPartidasA2(jugadorId));
        res.setPartidasA4(estadisticasRepository.findPartidasA4(jugadorId));
        res.setPartidasA6(estadisticasRepository.findPartidasA6(jugadorId));
        res.setNumeroFlores(estadisticasRepository.findNumeroFlores(jugadorId));
        res.setQuieros(estadisticasRepository.findQuieros(jugadorId));
        res.setNoQuieros(estadisticasRepository.findNoQuieros(jugadorId));
        return res;
    }


    
    
       
}
