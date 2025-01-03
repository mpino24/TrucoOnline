package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;


import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EstadisticasRepository extends CrudRepository<PartidaJugador, Integer> {
    
    @Query("SELECT COUNT(p) FROM PartidaJugador p WHERE p.player.id = :jugadorId AND p.game.instanteFin IS NOT NULL")
    Integer findAllPartidasJugadas(Integer jugadorId);

    @Query("SELECT SUM(TIMESTAMPDIFF(SECOND, p.game.instanteInicio, p.game.instanteFin)) " +
       "FROM PartidaJugador p " +
       "WHERE p.player.id = :jugadorId AND p.game.instanteFin IS NOT NULL")
    Integer findTiempoJugado(@Param("jugadorId") Integer jugadorId);


    //No me puedo creer que haya hecho esta megaquery y encima que funcione (o mejor dicho, que no de error)
    @Query("SELECT COUNT(p) FROM PartidaJugador p WHERE p.player.id = :jugadorId AND p.game.instanteFin IS NOT NULL AND ((p.posicion%2 = 0 AND p.game.puntosEquipo1 > p.game.puntosEquipo2) OR (p.posicion%2 = 1 AND p.game.puntosEquipo2 > p.game.puntosEquipo1))")
    Integer findVictorias(Integer jugadorId);


    @Query("SELECT COUNT(p) FROM PartidaJugador p WHERE p.player.id = :jugadorId AND p.game.numJugadores = 2")
    Integer findPartidasA2(Integer jugadorId); 
    @Query("SELECT COUNT(p) FROM PartidaJugador p WHERE p.player.id = :jugadorId AND p.game.numJugadores = 4")
    Integer findPartidasA4(Integer jugadorId);
    @Query("SELECT COUNT(p) FROM PartidaJugador p WHERE p.player.id = :jugadorId AND p.game.numJugadores = 6")
    Integer findPartidasA6 (Integer jugadorId);

    @Query("SELECT SUM(p.floresCantadas) FROM PartidaJugador p WHERE p.player.id = :jugadorId")
    Integer findNumeroFlores(Integer jugadorId);

    //TODO: Revisar como hacer los engaños
    //Integer findNumeroEnganos(Integer jugadorId);

    @Query("SELECT SUM(p.quierosCantados) FROM PartidaJugador p WHERE p.player.id = :jugadorId")
    Integer findQuieros (Integer jugadorId);

    @Query("SELECT SUM(p.noQuierosCantados) FROM PartidaJugador p WHERE p.player.id = :jugadorId")
    Integer findNoQuieros(Integer jugadorId);
}
