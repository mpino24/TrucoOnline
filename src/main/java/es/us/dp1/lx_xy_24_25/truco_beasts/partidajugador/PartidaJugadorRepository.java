package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

@Repository
public interface PartidaJugadorRepository extends CrudRepository<PartidaJugador, Integer>{

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.player.id = :id")
    PartidaJugador findPartidaJugadorbyId(Integer id);

    @Query("SELECT COUNT(pj) FROM PartidaJugador pj WHERE pj.game.id = :partidaId")
    Integer findNumJugadoresPartida(Integer partidaId);

    @Query("SELECT COUNT(pj) FROM PartidaJugador pj WHERE pj.player.id = :id AND pj.game.instanteFin IS NULL")
    Integer numberOfGamesConnected(Integer id);

    @Modifying
    @Query("DELETE FROM PartidaJugador pj WHERE pj.player.id = :userId AND pj.game.instanteFin IS NULL")
    void deleteByPlayerId(Integer userId);

    @Query("SELECT pj.posicion FROM PartidaJugador pj WHERE pj.game.id = :partidaId")
    List<Integer> lastPosition(Integer partidaId);

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.game.codigo= :partidaCode")
    List<PartidaJugador> findPlayersConnectedTo(String partidaCode);

    @Query("SELECT pj.game FROM PartidaJugador pj WHERE pj.player.id = :userId")
    Optional<Partida> findPartidaByUserId(Integer userId);



}
