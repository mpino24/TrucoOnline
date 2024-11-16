package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepository extends CrudRepository<Partida, Integer> {

    @Query("SELECT p FROM Partida p WHERE (p.instanteInicio IS NULL OR p.instanteFin IS NULL) AND p.visibilidad = PUBLICA")
    List<Partida> findAllPartidasActivas();

    @Query("SELECT p FROM Partida p WHERE (p.codigo = :codigo)")
    Optional<Partida> findPartidaByCodigo(String codigo);

}
