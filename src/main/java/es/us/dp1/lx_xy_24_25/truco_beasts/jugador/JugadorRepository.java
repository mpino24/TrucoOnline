package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;


import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;


public interface JugadorRepository extends Repository<Jugador,Integer>{
    List<Jugador> findAll();
    Optional<Jugador> findById(Integer id);
    Jugador findByUserId(Integer userId);
}

