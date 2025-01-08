package es.us.dp1.lx_xy_24_25.truco_beasts.chat;


import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter   
@Setter
public class ChatUsuario extends BaseEntity {
    
    @ManyToOne()
    @NotNull
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name= "chat_id", nullable=false)
    private Chat chat;

    @NotNull
    private LocalDateTime fecha;


    
}