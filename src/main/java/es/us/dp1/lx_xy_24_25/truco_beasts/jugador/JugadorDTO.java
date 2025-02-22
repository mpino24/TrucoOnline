package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.chat.MensajeDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorDTO {

    @NotEmpty
    Integer id;
    @NotEmpty
    String userName;
    @NotEmpty
    String email;
    @NotEmpty
    String firstName;
    @NotEmpty
    String lastName;
    @NotEmpty
    String photo;
    @NotNull
    Boolean isConnected;

    @Enumerated(EnumType.STRING)
    Amistad amistad;

    MensajeDTO ultimoMensaje;

    Integer mensajesSinLeer;


    public JugadorDTO(){}

    public JugadorDTO(Jugador j){
        this.id=j.getId();
        this.userName=j.getUser().getUsername();
        this.email=j.getEmail();
        this.firstName=j.getFirstName();
        this.lastName=j.getLastName();
        this.photo=j.getPhoto();
        this.isConnected=j.getUser().isConnected();

    }
    
}
