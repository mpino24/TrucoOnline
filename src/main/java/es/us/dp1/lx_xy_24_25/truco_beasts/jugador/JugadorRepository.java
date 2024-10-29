package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface JugadorRepository extends CrudRepository<Jugador,Integer>{
    Boolean existsById(int id);

    Optional<Jugador> findByUserId(Integer id);
    
    List<Jugador> findAll();
    Optional<Jugador> findById(Integer id);

    @Query("SELECT a FROM Jugador a WHERE (:id IN a.amigos)")
    Jugador findAmigosByUserId(Integer id);

}

