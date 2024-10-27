package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JugadorService {
    JugadorRepository jugadorRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository){
        this.jugadorRepository = jugadorRepository;
    }
    
    @Transactional(readOnly = true)
    public Collection<Jugador> encontrarTodos() {
        return (List<Jugador>) jugadorRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Jugador> encontrarJugadorPorId(int id) throws DataAccessException{
        return jugadorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Jugador encontrarJugadorPorUserId(Integer userId) throws DataAccessException{
        return jugadorRepository.findByUserId(userId);
    }
   
}
