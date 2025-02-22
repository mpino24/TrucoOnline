package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JugadorRepository extends CrudRepository<Jugador, Integer> {

    Boolean existsById(int id);

    @Query("SELECT j FROM Jugador j WHERE j.user.id = :userId")
    Optional<Jugador> findByUserId(Integer userId);

    List<Jugador> findAll();

    @Query("SELECT j FROM Jugador j WHERE :id = j.id")
    Optional<Jugador> findById(Integer id);

    @Query("SELECT a.amigos FROM Jugador a WHERE :id = a.user.id")
    List<Jugador> findAmigosByUserId(Integer id);

    @Query("SELECT j FROM Jugador j WHERE j.user.username = :userName")
    Optional<Jugador> findJugadorByUserName(String userName);

    @Query("SELECT a.solicitudes FROM Jugador a WHERE :id = a.user.id")
    List<Jugador> findSolicitudesByUserId(Integer id);

    @Query("SELECT j.user.username FROM Jugador j WHERE :id=j.id")
    String findUsernameByJugadorId(Integer id);

    @Query("SELECT j FROM Jugador j WHERE :jugador MEMBER OF j.solicitudes")
    List<Jugador> findSolicitantes(Jugador jugador);
}
