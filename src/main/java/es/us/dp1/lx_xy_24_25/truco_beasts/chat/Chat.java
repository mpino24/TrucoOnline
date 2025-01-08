package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Entity
@Getter
@Setter
public class Chat extends BaseEntity {
    @OneToOne
    Partida partida;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Mensaje> mensajes = new ArrayList<>();

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ChatUsuario> chatUsuarios = new ArrayList<>();

    public List<User> getUsuarios() {
        List<User> usuarios = new ArrayList<>();
        for (ChatUsuario chatUsuario : chatUsuarios) {
            usuarios.add(chatUsuario.getUser());
        }
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        for (User user : usuarios) {
            ChatUsuario chatUsuario = new ChatUsuario();
            chatUsuario.setUser(user);
            chatUsuario.setChat(this);
            chatUsuario.setFecha(LocalDateTime.now());
            chatUsuarios.add(chatUsuario);
        }
    }
    
}
