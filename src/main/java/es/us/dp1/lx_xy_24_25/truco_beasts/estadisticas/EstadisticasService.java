package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;


import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

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
        this.jugadorRepository = jugadorRepository;
        }

        @Transactional(readOnly = true)
        public EstadisticaJugador getEstadisticasJugador(Integer jugadorId) throws ResourceNotFoundException {
        Optional<Jugador> j = jugadorRepository.findById(jugadorId);
        if (j.isEmpty()) {
            throw new ResourceNotFoundException("El jugador de ID " + jugadorId + " no fue encontrado");
        }

        EstadisticaJugador res = new EstadisticaJugador();
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadas(jugadorId);
        Integer victorias = estadisticasRepository.findVictorias(jugadorId);
        Integer tiempoJugado = estadisticasRepository.findTiempoJugado(jugadorId);
        Integer partidasA2 = estadisticasRepository.findPartidasA2(jugadorId);
        Integer partidasA4 = estadisticasRepository.findPartidasA4(jugadorId);
        Integer partidasA6 = estadisticasRepository.findPartidasA6(jugadorId);
        Integer numeroFlores = estadisticasRepository.findNumeroFlores(jugadorId);
        Integer quiero = estadisticasRepository.findQuieros(jugadorId);
        Integer noQuiero = estadisticasRepository.findNoQuieros(jugadorId);
        Integer numeroEnganos = estadisticasRepository.findNumeroEnganos(jugadorId);
        Integer partidasConFlor = estadisticasRepository.findPartidasConFlor(jugadorId);
        Integer atrapado = estadisticasRepository.findNumeroAtrapado(jugadorId);

        res.setDerrotas(partidasJugadas == null ? 0 : partidasJugadas - (victorias == null ? 0 : victorias));
        res.setPartidasJugadas(partidasJugadas == null ? 0 : partidasJugadas);
        res.setTiempoJugado(tiempoJugado == null ? 0 : tiempoJugado);
        res.setVictorias(victorias == null ? 0 : victorias);
        res.setPartidasA2(partidasA2 == null ? 0 : partidasA2);
        res.setPartidasA4(partidasA4 == null ? 0 : partidasA4);
        res.setPartidasA6(partidasA6 == null ? 0 : partidasA6);
        res.setNumeroFlores(numeroFlores == null ? 0 : numeroFlores);
        res.setQuieros(quiero == null ? 0 : quiero);
        res.setNoQuieros(noQuiero == null ? 0 : noQuiero);
        res.setNumeroEnganos(numeroEnganos == null ? 0 : numeroEnganos);
        res.setPartidasConFlor(partidasConFlor == null ? 0 : partidasConFlor);
        res.setAtrapado(atrapado == null ? 0 : atrapado);

        return res;
        }

        @Transactional(readOnly = true)
        public EstadisticaGlobal getEstadisticasGlobales() {
        EstadisticaGlobal res = new EstadisticaGlobal();
        
        Integer tiempoJugado = estadisticasRepository.findTiempoJugadoGlobal();
        Integer victorias = estadisticasRepository.findVictoriasGlobal();
        Integer partidasJugadas = estadisticasRepository.findAllPartidasJugadasGlobal();
        Integer partidasA2 = estadisticasRepository.findPartidasA2Global();
        Integer partidasA4 = estadisticasRepository.findPartidasA4Global();
        Integer partidasA6 = estadisticasRepository.findPartidasA6Global();
        Integer numeroFlores = estadisticasRepository.findNumeroFloresGlobal();
        Integer numeroEnganos = estadisticasRepository.findNumeroEnganosGlobal();
        Integer quiero = estadisticasRepository.findQuierosGlobal();
        Integer noQuiero = estadisticasRepository.findNoQuierosGlobal();
        Integer jugadoresTotales = estadisticasRepository.findJugadoresTotales();
        Integer partidasConFlor = estadisticasRepository.findPartidasConFlor();
        Integer atrapado = estadisticasRepository.findNumeroAtrapadoGlobal();

        res.setTiempoJugado(tiempoJugado == null ? 0 : tiempoJugado);
        res.setVictorias(victorias == null ? 0 : victorias);
        res.setDerrotas(partidasJugadas == null ? 0 : partidasJugadas - (victorias == null ? 0 : victorias));
        res.setPartidasJugadas(partidasJugadas == null ? 0 : partidasJugadas);
        res.setPartidasA2(partidasA2 == null ? 0 : partidasA2);
        res.setPartidasA4(partidasA4 == null ? 0 : partidasA4);
        res.setPartidasA6(partidasA6 == null ? 0 : partidasA6);
        res.setNumeroFlores(numeroFlores == null ? 0 : numeroFlores);
        res.setNumeroEnganos(numeroEnganos == null ? 0 : numeroEnganos);
        res.setQuieros(quiero == null ? 0 : quiero);
        res.setNoQuieros(noQuiero == null ? 0 : noQuiero);
        res.setJugadoresTotales(jugadoresTotales == null ? 0 : jugadoresTotales);
        res.setPartidasConFlor(partidasConFlor == null ? 0 : partidasConFlor);
        res.setAtrapado(atrapado == null ? 0 : atrapado);

        return res;
        
    }

   public List<DatosPorPartida> getEstadisticasJugadorAvanzadas(Integer jugadorId) {
    Optional<Jugador> j = jugadorRepository.findById(jugadorId);
    if (j.isEmpty()) {
        throw new ResourceNotFoundException("El jugador de ID " + jugadorId + " no fue encontrado");
    }
    List<Object[]> rawData = estadisticasRepository.findAllDatosPorPartidaByJugadorId(jugadorId);

    if (rawData == null) {
        throw new ResourceNotFoundException("Los datos por partida del jugador con ID " + jugadorId + " no fueron encontrados");
    }

    List<DatosPorPartida> res = rawData.stream()
        .map(row -> new DatosPorPartida(
            (Boolean) row[0],
            (LocalDateTime) row[1],
            (Boolean) row[2],
            (Integer) row[3],
            (Integer) row[4],
            (Integer)row[5]))
        .collect(Collectors.toList());

    return res;
}

public List<JugadorVictorias> getRankingGlobal(Integer topPedido) { // Por defecto, top 10
    List<Object[]> rawData = estadisticasRepository.findVictoriasPorJugador();

    int topLimit = (topPedido == null) ? 10 : topPedido;

    rawData = rawData.stream().limit(topLimit).collect(Collectors.toList());

    List<JugadorVictorias> res = rawData.stream()
            .map(row -> new JugadorVictorias(row[0] != null? jugadorRepository.findUsernameByJugadorId((Integer) row[0]): "Desconocido", (Long) row[1])) 
            .collect(Collectors.toList());

    return res;
}




}
