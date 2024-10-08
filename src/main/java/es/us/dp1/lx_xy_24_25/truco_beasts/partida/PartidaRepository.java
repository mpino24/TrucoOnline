package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PartidaRepository extends CrudRepository<Partida, Integer> {
    @Query("SELECT p FROM Partida p WHERE (p.estado = ACTIVE OR p.estado = WAITING) AND p.visibilidad = PUBLICA")
    List<Partida> findAllPartidasActivas();
}
