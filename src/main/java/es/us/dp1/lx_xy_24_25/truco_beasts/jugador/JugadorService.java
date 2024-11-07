package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public JugadorDTO findJugadorByUserId(int userId) throws DataAccessException{
        Optional<Jugador> j=jugadorRepository.findByUserId(userId);
        if(j.isEmpty()){
            throw new ResourceNotFoundException("El jugador de ID "+userId+" no fue encontrado");
        }else{
            JugadorDTO res= new JugadorDTO(j.get());
        return res;
        }
        
        
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
        Optional<Jugador> j=jugadorRepository.findByUserId(user.getId());
        if(j.isEmpty()){
            throw new ResourceNotFoundException("El jugador de ID "+user.getId()+" no fue encontrado");
        }else{
            Jugador toUpdate = j.get();
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

    public List<JugadorDTO> findAmigosByUserId(int userId){
        return jugadorRepository.findAmigosByUserId(userId);
    }

    public JugadorDTO findJugadorByUserName(String userName){
        return jugadorRepository.findJugadorByUserName(userName);
    }

    public boolean checkIfAreFriends(String friendUserName, int userId){
        List<JugadorDTO> amigos = jugadorRepository.findAmigosByUserId(userId);
        return (amigos.stream().map(a-> a.getUserName()).toList().contains(friendUserName));

    }

}

