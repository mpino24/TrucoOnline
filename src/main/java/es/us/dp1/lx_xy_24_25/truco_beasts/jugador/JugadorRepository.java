package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface JugadorRepository extends CrudRepository<Jugador, Integer> {

    Boolean existsById(int id);

    Optional<Jugador> findByUserId(Integer id);

    List<Jugador> findAll();

    Optional<Jugador> findById(Integer id);

    @Query("SELECT a.amigos FROM Jugador a WHERE :id = a.id")
    List<JugadorDTO> findAmigosByUserId(Integer id);

    @Query("SELECT j FROM Jugador j WHERE j.user.username = :userName")
    JugadorDTO findJugadorByUserName(String userName);
}
