package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PartidaJugadorRepository extends CrudRepository<PartidaJugador, Integer>{

    @Query("SELECT pj FROM PartidaJuagdor pj WHERE pj.jugador.id == :id")
    PartidaJugador findPartidaJugardorbyId(Integer id);

}
