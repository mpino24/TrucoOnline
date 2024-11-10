package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaJugadorRepository extends CrudRepository<PartidaJugador, Integer>{

    @Query("SELECT pj FROM PartidaJugador pj WHERE pj.player.id = :id")
    PartidaJugador findPartidaJugadorbyId(Integer id);

    @Query("SELECT COUNT(pj) FROM PartidaJugador pj WHERE pj.game.id = :partidaId")
    Integer findNumJugadoresPartida(Integer partidaId);

    @Query("SELECT COUNT(pj) FROM PartidaJugador pj WHERE pj.player.id = :id AND pj.game.instanteFin IS NULL")
    Integer numberOfGamesConnected(Integer id);

    @Modifying
    @Query("DELETE FROM PartidaJugador pj WHERE pj.player.id = :userId")
    void deleteByPlayerId(Integer userId);


}
