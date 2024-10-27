package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = PerfilSerializer.class)
@JsonDeserialize(using = PerfilDeserializer.class)
public class PerfilJugadorUsuario {
    private Jugador jugador;
    private User user;
 
}
