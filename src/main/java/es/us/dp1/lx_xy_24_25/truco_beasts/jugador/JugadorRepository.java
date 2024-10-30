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

   @Query("SELECT a.amigos FROM Jugador a WHERE :id = a.id")
    //@Query("SELECT * FROM Jugador j1 INNER JOIN Amigo a2 ON t1.userId = a2.jugadorId WHERE a2.userId = :id ")
    List<Jugador> findAmigosByUserId(Integer id);

}

