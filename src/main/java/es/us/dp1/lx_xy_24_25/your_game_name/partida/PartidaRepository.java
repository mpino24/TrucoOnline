package es.us.dp1.lx_xy_24_25.your_game_name.partida;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PartidaRepository extends CrudRepository<Partida, Integer> {
    @Query("SELECT p FROM Partida p WHERE p.estado = ACTIVE || p.estado = WAITING && p.visibilidad = PUBLICA")
    List<Partida> findAllPartidasActivas();
}
