package es.us.dp1.lx_xy_24_25.truco_beasts.chat;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Service
public class ChatService {

    private final MensajeRepository mensajeRepository;
    private final UserService userService;
    private final PartidaJugadorService partJugService;

    public ChatService(MensajeRepository mensajeRepository,UserService userService,PartidaJugadorService partJugService) {
        this.mensajeRepository = mensajeRepository;
        this.userService=userService;
        this.partJugService=partJugService;
    }
    public Mensaje guardarMensaje(Mensaje mensaje) throws NotYourChatException {
        User usuarioActual=userService.findCurrentUser();
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(usuarioActual);
        Chat chatDestino = mensaje.getChat();

        if(!chatDestino.getUsuarios().isEmpty()){
            if(!chatDestino.getUsuarios().contains(usuarioActual)){
                throw new NotYourChatException("No formas parte de este chat");
            }
        }else if(mensaje.getChat().getPartida()!=null){
            if(!partJugService.getPartidaOfUserId(usuarioActual.getId()).getId().equals(mensaje.getChat().getPartida().getId())){
                throw new NotYourChatException("No formas parte de este chat");
            }
        }else{
            throw new IllegalArgumentException("El mensaje pasado como parámetro debe estar vinculado a una partida o a una lista de usuarios");
        }
        return mensajeRepository.save(mensaje);
    }

    public void createChat(Chat chat) {
       // return chatRepository.save(chat);
    }

}
