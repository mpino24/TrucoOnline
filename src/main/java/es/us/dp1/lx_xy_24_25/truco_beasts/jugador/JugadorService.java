package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class JugadorService {
    JugadorRepository jugadorRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository){
        this.jugadorRepository = jugadorRepository;
    }
    
    @Transactional(readOnly = true)
    public Collection<Jugador> findAll() {
        return (List<Jugador>) jugadorRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Jugador findJugadorById(int id) throws DataAccessException{
        return jugadorRepository.findById(id).orElseThrow(
            ()-> new ResourceNotFoundException("Jugador", "id", id));
    }
    @Transactional(readOnly = true)
    public Jugador findJugadorByUserId(int userId) throws DataAccessException{
        return jugadorRepository.findByUserId(userId).orElseThrow(
            ()-> new ResourceNotFoundException("Jugador", "userid", userId));
    }

    @Transactional(readOnly =true)
	public Boolean existsJugador(int id) {
		return jugadorRepository.existsById(id);
	}

    @Transactional
	public Jugador saveJugador( Jugador jugador) throws DataAccessException {
		
        Jugador savedJugador = jugadorRepository.save(jugador);
    
     
		return savedJugador;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, DataAccessException.class})
    public Jugador updateJugador(@RequestBody @Valid Jugador jugador, User user){
        Jugador toUpdate = findJugadorByUserId(user.getId());
        
        if (toUpdate == null) {
            throw new EntityNotFoundException("Jugador no encontrado para el usuario: " + user.getUsername());
        }else{
            BeanUtils.copyProperties(jugador, toUpdate, "id","user","amigos");
            
         if (jugador.getAmigos() != null) {
        
            toUpdate.getAmigos().clear();  

        
            for (Jugador amigo : jugador.getAmigos()) {
            toUpdate.getAmigos().add(amigo);  
            amigo.getAmigos().add(toUpdate);   
            }
        }
            saveJugador(toUpdate);
            return toUpdate;
        }  
    }

}

