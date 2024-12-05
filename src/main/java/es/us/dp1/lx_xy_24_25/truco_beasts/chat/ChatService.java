package es.us.dp1.lx_xy_24_25.truco_beasts.chat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Service
public class ChatService {

    private final MensajeRepository mensajeRepository;
    private final UserService userService;
    private final PartidaJugadorService partJugService;
    private final ChatRepository chatRepository;

    public ChatService(MensajeRepository mensajeRepository,UserService userService,PartidaJugadorService partJugService, ChatRepository chatRepository) {
        this.mensajeRepository = mensajeRepository;
        this.userService=userService;
        this.partJugService=partJugService;
        this.chatRepository =chatRepository;
    }
    public Mensaje guardarMensaje(Mensaje mensaje) throws NotYourChatException {
        User usuarioActual=userService.findCurrentUser();
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(usuarioActual);

        perteneceAChat(mensaje.getChat().getId());

        return mensajeRepository.save(mensaje);
    }

    public void createChat(Chat chat) throws NotYourChatException{
        perteneceAChat(chat.getId());
        chatRepository.save(chat);
    }

    public List<Mensaje> getMensajesDe(Integer chatId) throws NotYourChatException{
        perteneceAChat(chatId);
        return mensajeRepository.findMessagesFrom(chatId);
    }

    public Mensaje getLastMessage(Integer chatId) throws NotYourChatException{
        perteneceAChat(chatId);
        return mensajeRepository.findLastMessage(chatId).orElse(null);
    }


    public void perteneceAChat(Integer chatId) throws NotYourChatException{
        User usuarioActual=userService.findCurrentUser();
        Chat chatDestino = chatRepository.findById(chatId).orElseThrow(()-> new ResourceNotFoundException("Chat no encontrado"));

        if(!chatDestino.getUsuarios().isEmpty()){
            if(!chatDestino.getUsuarios().contains(usuarioActual)){
                throw new NotYourChatException("No formas parte de este chat");
            }
        }else if(chatDestino.getPartida()!=null){
            if(!partJugService.getPartidaOfUserId(usuarioActual.getId()).getId().equals(chatDestino.getPartida().getId())){
                throw new NotYourChatException("No formas parte de este chat");
            }
        }else{
            throw new IllegalArgumentException("El mensaje pasado como parámetro debe estar vinculado a una partida o a una lista de usuarios");
        }

    }

}
