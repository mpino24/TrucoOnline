package es.us.dp1.lx_xy_24_25.truco_beasts.chat;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final MensajeRepository mensajeRepository;

    public ChatService(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }
    public Mensaje guardarMensaje(Mensaje mensaje,Chat chat) {
        mensaje.setChat(chat); //Comprobar que pertenece al chat
        mensaje.setFechaEnvio(LocalDateTime.now());
        return mensajeRepository.save(mensaje);
    }

    public void createChat(Chat chat) {
       // return chatRepository.save(chat);
    }

}
