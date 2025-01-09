package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDTO {

    Integer id;

    Partida partida;

    List<UserDTO> usuarios; 

    List<MensajeDTO> mensajes;

    public ChatDTO() {

    }

    public ChatDTO(Chat chat) {
        this.id = chat.getId();
        this.partida = chat.getPartida();
        this.usuarios = new ArrayList<>();
        for (User user : chat.getUsuarios()) {
            this.usuarios.add(new UserDTO(user));
        }
        this.mensajes = new ArrayList<>();
        for (Mensaje mensaje : chat.getMensajes()) {
            this.mensajes.add(new MensajeDTO(mensaje));
        }
    }
    
}
