package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaJugadorRepository extends CrudRepository<PartidaJugador, Integer>{

    @Query("SELECT pj FROM PartidaJuagdor pj WHERE pj.jugador.id == :id")
    PartidaJugador findPartidaJugardorbyId(Integer id);

}
