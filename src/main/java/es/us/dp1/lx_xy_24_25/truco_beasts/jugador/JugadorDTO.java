package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

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

    public JugadorDTO(){}

    public JugadorDTO(Jugador j){
        this.id=j.getId();
        this.userName=j.getUser().getUsername();
        this.email=j.getEmail();
        this.firstName=j.getFirstName();
        this.lastName=j.getLastName();
        this.photo=j.getPhoto();
        this.isConnected=j.getUser().getIsConnected();

    }
    
}
