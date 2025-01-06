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

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.player.id = :playerId AND pj.game.instanteFin IS NULL")
    PartidaJugador findByPlayerIdAndGameNotFinish(Integer playerId);

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

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.game.id=:partidaId AND pj.isCreator = true")
    Optional<PartidaJugador> findCreator(Integer partidaId);

    @Query("SELECT pj.game FROM PartidaJugador pj WHERE pj.game.codigo=:codigo")
    Optional<Partida> findPartidaByCodigoPartida(String codigo);

    @Query("SELECT new es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView(j.user.username, j.photo, pj.posicion,pj.game.id) FROM PartidaJugador pj JOIN pj.player j JOIN pj.game p WHERE p.codigo=:codigo")
    List<PartidaJugadorView> findAllJugadoresPartida(String codigo);

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.game.codigo = :codigo AND pj.posicion = :posicion")
    PartidaJugador findPartidaJugadorByCodigoPartidaAndPosicionJugador(String codigo, Integer posicion);

}
