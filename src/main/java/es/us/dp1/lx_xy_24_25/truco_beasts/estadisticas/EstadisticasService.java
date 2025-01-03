package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

    
    private final EstadisticasRepository estadisticasRepository;

    public EstadisticasService(EstadisticasRepository estadisticasRepository) {
        this.estadisticasRepository = estadisticasRepository;
    }

    public EstadisticaJugador getEstadisticasJugador(Integer jugadorId){
        EstadisticaJugador estadisticaJugador = new EstadisticaJugador();
        estadisticaJugador.partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugadorId);
        estadisticaJugador.tiempoJugado = estadisticasRepository.findTiempoJugado(jugadorId);
        estadisticaJugador.victorias = estadisticasRepository.findVictorias(jugadorId);
        estadisticaJugador.derrotas = estadisticaJugador.partidasJugadas - estadisticaJugador.victorias;
        estadisticaJugador.partidasA2 = estadisticasRepository.findPartidasA2(jugadorId);
        estadisticaJugador.partidasA4 = estadisticasRepository.findPartidasA4(jugadorId);
        estadisticaJugador.partidasA6 = estadisticasRepository.findPartidasA6(jugadorId);
        estadisticaJugador.numeroFlores = estadisticasRepository.findNumeroFlores(jugadorId);
        //estadisticaJugador.numeroEnganos = estadisticasRepository.findNumeroEnganos(jugadorId);
        estadisticaJugador.quieros = estadisticasRepository.findQuieros(jugadorId);
        estadisticaJugador.noQuieros = estadisticasRepository.findNoQuieros(jugadorId);
        return estadisticaJugador;
    }
}
