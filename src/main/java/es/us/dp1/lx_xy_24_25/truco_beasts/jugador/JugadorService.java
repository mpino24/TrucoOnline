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
    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    @Transactional(readOnly = true)
    public Collection<Jugador> findAll() {
        return (List<Jugador>) jugadorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Jugador findJugadorById(int id) throws DataAccessException {
        return jugadorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Jugador", "id", id));
    }

    @Transactional(readOnly = true)
    public JugadorDTO findJugadorByUserId(int userId) throws DataAccessException {
        Optional<Jugador> j = jugadorRepository.findByUserId(userId);
        if (j.isEmpty()) {
            throw new ResourceNotFoundException("El jugador de ID " + userId + " no fue encontrado");
        } else {
            JugadorDTO res = new JugadorDTO(j.get());
            return res;
        }

    }

    @Transactional(readOnly = true)
    public Boolean existsJugador(int id) {
        return jugadorRepository.existsById(id);
    }

    @Transactional
    public Jugador saveJugador(Jugador jugador) throws DataAccessException {

        Jugador savedJugador = jugadorRepository.save(jugador);

        return savedJugador;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, DataAccessException.class})
    public Jugador updateJugador(@RequestBody @Valid Jugador jugador, User user) {
        Optional<Jugador> j = jugadorRepository.findByUserId(user.getId());
        if (j.isEmpty()) {
            throw new ResourceNotFoundException("El jugador de ID " + user.getId() + " no fue encontrado");
        } else {
            Jugador toUpdate = j.get();
            BeanUtils.copyProperties(jugador, toUpdate, "id", "user", "amigos");
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

    @Transactional(readOnly = true)
    public List<JugadorDTO> findAmigosByUserId(int userId) {
        return jugadorRepository.findAmigosByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<JugadorDTO> findSolicitudesByUserId(int userId) {
        return jugadorRepository.findSolicitudesByUserId(userId);
    }

    @Transactional(readOnly = true)
    public JugadorDTO findJugadorByUserName(String userName) {
        Optional<JugadorDTO> res = jugadorRepository.findJugadorByUserName(userName);
        if (res.isEmpty()) {
            throw new ResourceNotFoundException("Jugador no encontrado");
        } else {
            return res.get();
        }

    }

    @Transactional(readOnly = true)
    public boolean checkIfAreFriends(String friendUserName, int userId) {
        List<JugadorDTO> amigos = jugadorRepository.findAmigosByUserId(userId);
        return (amigos.stream().map(a -> a.getUserName()).toList().contains(friendUserName));

    }
    @Transactional(readOnly = true)
    public boolean comprobarExistenciaSolicitud(String friendUserName, int userId) {
        List<JugadorDTO> solicitudes = jugadorRepository.findSolicitudesByUserId(userId);
        return (solicitudes.stream().map(a -> a.getUserName()).toList().contains(friendUserName));

    }

    @Transactional()
    public void addNewFriends(int userId, int amigoPlayerId) {
        Optional<Jugador> jugadorOpt = jugadorRepository.findByUserId(userId);
        Optional<Jugador> amigoOpt = jugadorRepository.findById(amigoPlayerId);
        if (!jugadorOpt.isEmpty() && !amigoOpt.isEmpty()) {
            Jugador jugador = jugadorOpt.get();
            Jugador amigo = amigoOpt.get();
            if (!jugador.getAmigos().contains(amigo)) {
                if (!jugador.getId().equals(amigo.getId())) {
                    jugador.getAmigos().add(amigo);
                    amigo.getAmigos().add(jugador);
                    jugador.getSolicitudes().remove(amigo);
                    jugadorRepository.save(jugador);
                    jugadorRepository.save(amigo);
                } else {
                    throw new IllegalStateException("No te puedes agregar a ti mismo");
                }
            } else {
                throw new IllegalStateException("Ya sois amigos!!");
            }
        } else {
            throw new ResourceNotFoundException("Usuarios no encontrados");
        }

    }

    @Transactional
    public void crearSolicitud(int userId, int solicitadoId){
        Optional<Jugador> jugadorOpt = jugadorRepository.findByUserId(userId);
        Optional<Jugador> solicitadoOpt = jugadorRepository.findById(solicitadoId);
        if (!jugadorOpt.isEmpty() && !solicitadoOpt.isEmpty()) {
            Jugador jugador = jugadorOpt.get();
            Jugador solicitado = solicitadoOpt.get();
            if (!jugador.getSolicitudes().contains(solicitado)) {
                if (!jugador.getId().equals(solicitado.getId())) {
                    solicitado.getSolicitudes().add(jugador);
                    jugadorRepository.save(solicitado);
                } else {
                    throw new IllegalStateException("No te puedes agregar a ti mismo");
                }
            } else {
                throw new IllegalStateException("Ya sois amigos!!");
            }
        } else {
            throw new ResourceNotFoundException("Usuarios no encontrados");
        }
    }

    @Transactional()
    public void deleteFriends(int userId, int amigoPlayerId) {
        Optional<Jugador> jugadorOpt = jugadorRepository.findByUserId(userId);
        Optional<Jugador> amigoOpt = jugadorRepository.findById(amigoPlayerId);
        if (!jugadorOpt.isEmpty() && !amigoOpt.isEmpty()) {
            Jugador jugador = jugadorOpt.get();
            Jugador amigo = amigoOpt.get();
            if (jugador.getAmigos().contains(amigo)) {
                if (!jugador.getId().equals(amigo.getId())) {
                    jugador.getAmigos().remove(amigo);
                    amigo.getAmigos().remove(jugador);
                    jugadorRepository.save(jugador);
                    jugadorRepository.save(amigo);
                }
            }
        } else {
            throw new ResourceNotFoundException("Usuarios no encontrados");
        }
    }
    @Transactional()
    public void deleteSolicitud(int userId, int solicitadoId) {
        Optional<Jugador> jugadorOpt = jugadorRepository.findByUserId(userId);
        Optional<Jugador> solicitanteOpt = jugadorRepository.findById(solicitadoId);
        if (!jugadorOpt.isEmpty() && !solicitanteOpt.isEmpty()) {
            Jugador jugador = jugadorOpt.get();
            Jugador solicitante = solicitanteOpt.get();
            if (jugador.getSolicitudes().contains(solicitante)) {
                if (!jugador.getId().equals(solicitante.getId())) {
                    jugador.getSolicitudes().remove(solicitante);
                    jugadorRepository.save(jugador);
                }
            }
        } else {
            throw new ResourceNotFoundException("Usuarios no encontrados");
        }
    }
}
