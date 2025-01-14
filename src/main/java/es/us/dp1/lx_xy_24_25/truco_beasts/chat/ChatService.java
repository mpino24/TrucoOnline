package es.us.dp1.lx_xy_24_25.truco_beasts.chat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ChatUsuarioRepository chatUsuarioRepository;

    public ChatService(MensajeRepository mensajeRepository,UserService userService,PartidaJugadorService partJugService, ChatRepository chatRepository,ChatUsuarioRepository chatUsuarioRepository){
        this.mensajeRepository = mensajeRepository;
        this.userService=userService;
        this.partJugService=partJugService;
        this.chatRepository =chatRepository;
        this.chatUsuarioRepository=chatUsuarioRepository;
    
    }
    public MensajeDTO guardarMensaje(Mensaje mensaje) throws NotYourChatException {
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(userService.findUser(mensaje.getRemitente().getId()));
        Chat chat = chatRepository.findById(mensaje.getChat().getId()).orElse(null);
        if(chat==null){
            throw new NotYourChatException("No puedes enviar mensajes a un chat que no existe");
        }
        mensaje.setChat(chat);
        return new MensajeDTO(mensajeRepository.save(mensaje));
    }

    public void createChat(Chat chat){
        chatRepository.save(chat);
    }

    public List<MensajeDTO> getMensajesDe(Integer chatId) throws NotYourChatException{
        //perteneceAChat(chatId);
        List<Mensaje> mensajes=mensajeRepository.findMessagesFrom(chatId);
        List<MensajeDTO> mensajesDTO= mensajes.stream().map(MensajeDTO::new).toList();
        return mensajesDTO;
    }

    public MensajeDTO getLastMessage(Integer chatId){
        return mensajeRepository.findLastMessage(chatId).map(MensajeDTO::new).orElse(null);  
    }

    public Chat findChatWith(Integer amigoId){
        return chatRepository.findChatBetween(amigoId,userService.findCurrentUser().getId());

    }

    public List<User> findUsersByChat(Integer chatId){
        return chatRepository.findUsersByChat(chatId);
    }

    @Transactional
    public void eliminarChat(Integer chatId){
        chatRepository.deleteById(chatId);
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

    @Transactional
    public void updateChatTime(Integer chatId){
        Optional<ChatUsuario> chatUsuario = chatUsuarioRepository.findChatUsuarioByUserAndChat(userService.findCurrentUser().getId(), chatId);
        if(chatUsuario.isPresent()){
            chatUsuario.get().setFecha(LocalDateTime.now());
            chatUsuarioRepository.save(chatUsuario.get());
        }else{
            throw new ResourceNotFoundException("No se ha encontrado el chatUsuario");
        }
    }

    @Transactional(readOnly = true)
    public Integer findNumNotReadMessages(Integer chatId){
        LocalDateTime lastConnection = chatUsuarioRepository.findChatUsuarioByUserAndChat(userService.findCurrentUser().getId(), chatId).get().getFecha();
        return mensajeRepository.findMessagesAfter(chatId,lastConnection,userService.findCurrentUser().getId());
    }

    @Transactional(readOnly = true)
    public Integer findNumAllNotReadMessages(){
        List<ChatUsuario> chatsUsuario = chatUsuarioRepository.findChatUsuarioByUser(userService.findCurrentUser().getId());
        Integer numMessages = 0;
        for(ChatUsuario chatUsuario: chatsUsuario){
            numMessages+=findNumNotReadMessages(chatUsuario.getChat().getId());
        }
        return numMessages;
    }

}
