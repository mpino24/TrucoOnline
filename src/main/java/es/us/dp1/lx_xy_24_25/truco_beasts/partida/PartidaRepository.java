package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidaRepository extends CrudRepository<Partida, Integer> {

    @Query("SELECT p FROM Partida p WHERE (p.instanteInicio IS NULL OR p.instanteFin IS NULL) AND p.visibilidad = PUBLICA")
    Page<Partida> findAllPartidasActivas(Pageable pageable);

    @Query("SELECT p FROM Partida p WHERE (p.codigo = :codigo)")
    Optional<Partida> findPartidaByCodigo(String codigo);

}
