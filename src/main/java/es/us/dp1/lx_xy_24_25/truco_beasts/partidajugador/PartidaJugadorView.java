package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaJugadorView {
    

    String userName;
    String foto;

    public PartidaJugadorView(String userName, String foto) {
        this.userName = userName;
        this.foto = foto;
    }
    
    
}
