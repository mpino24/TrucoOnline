package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.chat.Chat;
import es.us.dp1.lx_xy_24_25.truco_beasts.chat.ChatService;
import es.us.dp1.lx_xy_24_25.truco_beasts.chat.MensajeDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import jakarta.persistence.EntityNotFoundException;


@Service
public class JugadorService {

    JugadorRepository jugadorRepository;
    ChatService chatService;
    UserService userService;
    PartidaJugadorRepository partidaJugadorRepository;

    @Autowired
    public JugadorService(JugadorRepository jugadorRepository, ChatService chatService, UserService userService, PartidaJugadorRepository partidaJugadorRepository) {
        this.jugadorRepository = jugadorRepository;
        this.chatService = chatService;
        this.userService = userService;
        this.partidaJugadorRepository = partidaJugadorRepository;
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
    public Jugador findJugadorByUserId(int userId) throws DataAccessException {
        Optional<Jugador> jugador = jugadorRepository.findByUserId(userId); 
        if(jugador.isEmpty()){
            throw new ResourceNotFoundException("Jugador no encontrado");
        }
        return jugador.get();

    }

    public JugadorDTO findJugadorDTOByUserId(int userId) {
        return convertirJugadorADto(findJugadorByUserId(userId));
    }

    @Transactional(readOnly = true)
    public Boolean existsJugador(int id) {
        return jugadorRepository.existsById(id);
    }

    @Transactional
    public Jugador saveJugador(Jugador jugador) throws DataAccessException {

        jugadorRepository.save(jugador);

        return jugador;
    }

    @Transactional(rollbackFor = {EntityNotFoundException.class, DataAccessException.class})
    public Jugador updateJugador( Jugador jugador, User user) {
        Optional<Jugador> j = jugadorRepository.findByUserId(user.getId());
        if (j.isEmpty()) {
            throw new ResourceNotFoundException("El jugador de ID " + user.getId() + " no fue encontrado");
        } else {
            Jugador toUpdate = j.get();
            BeanUtils.copyProperties(jugador, toUpdate, "id", "user", "amigos", "solicitudes");
            saveJugador(toUpdate);
            return toUpdate;

        }
    }

    @Transactional(readOnly = true)
    public List<JugadorDTO> findAmigosByUserId(int userId) {
        List<Jugador> amigos= jugadorRepository.findAmigosByUserId(userId);
        return amigos.stream()
                 .map(j -> convertirJugadorADto(j))
                 .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<JugadorDTO> findSolicitudesByUserId(int userId) {
        List<Jugador> solicitudes= jugadorRepository.findSolicitudesByUserId(userId);
        return solicitudes.stream()
                 .map(j -> convertirJugadorADto(j))
                 .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JugadorDTO findJugadorByUserName(String userName){
        Optional<Jugador> res = jugadorRepository.findJugadorByUserName(userName);
        if (res.isEmpty()) {
            throw new ResourceNotFoundException("Jugador no encontrado");
        } else {
            return convertirJugadorADto(res.get());
        }

    }

    @Transactional(readOnly = true)
    public boolean checkIfAreFriends(Jugador jugador1, Jugador jugador2) throws DataAccessException{
        List<Jugador> amigos = jugadorRepository.findAmigosByUserId(jugador1.getId());
        return amigos.stream().anyMatch(a -> a.getId().equals(jugador2.getId()));

    }

    @Transactional(readOnly = true)
    public boolean comprobarExistenciaSolicitud(Jugador jugador1, Jugador jugador2) throws DataAccessException {
        List<Jugador> solicitudes = jugadorRepository.findSolicitudesByUserId(jugador1.getId());
        return solicitudes.stream().anyMatch(a -> a.getId().equals(jugador2.getId()));

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
                    //Crear entidad de chat entre amigos
                    Chat chat = new Chat();
                    List<User> usuarios = new ArrayList<>();
                    usuarios.add(userService.findCurrentUser());
                    usuarios.add(userService.findUser(amigo.getId()));
                    chat.setUsuarios(usuarios);
                    chatService.createChat(chat);
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
    public void crearSolicitud(int userId, int solicitadoId) {
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

                    Integer chatId = chatService.findChatWith(amigo.getId()).getId();
                    chatService.eliminarChat(chatId);

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

    public Jugador findCurrentPlayer(){
        User currentUser = userService.findCurrentUser();
        return jugadorRepository.findByUserId(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("No se encontro al jugador asociado a esa userId"));
    }

    public JugadorDTO convertirJugadorADto(Jugador  j) {
        Jugador jugadorActual = findCurrentPlayer();
        
        JugadorDTO res = new JugadorDTO(j);
        res.setUltimoMensaje(null);
        res.setAmistad(null);

        if (!j.getId().equals(jugadorActual.getId())) {
            if (checkIfAreFriends(j,jugadorActual)) {
                res.setAmistad(Amistad.AMIGOS);
                Chat chat = chatService.findChatWith(j.getId());
                if (chat != null) { // Verifica si el chat no es nulo
                    MensajeDTO mensaje = chatService.getLastMessage(chat.getId());
                    if (mensaje != null) { // Verifica si el mensaje no es nulo
                        res.setUltimoMensaje(mensaje);
                    }
                    Integer mensajesSinLeer = chatService.findNumNotReadMessages(chat.getId());
                    res.setMensajesSinLeer(mensajesSinLeer);
                }
            } else if (comprobarExistenciaSolicitud(j,jugadorActual) || comprobarExistenciaSolicitud(jugadorActual,j)) {
                res.setAmistad(Amistad.SOLICITADO);
            } else {
                res.setAmistad(Amistad.DESCONOCIDOS);
            }
        }

        return res;

    }

    @Transactional
    public void deleteJugadorByUserId(Integer userId) {
        Jugador jugador = jugadorRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado"));
    
        List<PartidaJugador> partidasJugador = partidaJugadorRepository.findByJugadorId(userId);
        for (PartidaJugador partidaJugador : partidasJugador) {
            partidaJugadorRepository.delete(partidaJugador);  // Eliminar las participaciones en partidas
        }
    
        List<Jugador> amigos = jugadorRepository.findAmigosByUserId(userId);
        for (Jugador amigo : amigos) {
            amigo.getAmigos().remove(jugador);  // Eliminar al jugador de la lista de amigos del amigo
            jugador.getAmigos().remove(amigo);  
            jugadorRepository.save(amigo);
            
            Chat chat= chatService.findChatWith(amigo.getId());
            if(chat!=null){
                chatService.eliminarChat(chat.getId());
            }
        }

        
    
    
        // Limpiar la lista de solicitudes enviadas del jugador a eliminar
        List<Jugador> solicitudesEnviadas = jugadorRepository.findSolicitantes(jugador);
        for(Jugador solicitado: solicitudesEnviadas){
            solicitado.getSolicitudes().remove(jugador);
            jugadorRepository.save(solicitado);
        }

    
        // Vaciar la lista de solicitudes del jugador
        jugador.getSolicitudes().clear();  // Limpiar la lista de solicitudes del jugador
        jugadorRepository.save(jugador);   // Guardar los cambios del jugador
    
        // Finalmente, eliminar al jugador
        jugadorRepository.delete(jugador);
    }
    
}
