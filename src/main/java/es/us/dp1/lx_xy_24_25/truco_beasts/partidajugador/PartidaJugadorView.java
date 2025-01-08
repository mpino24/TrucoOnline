package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaJugadorView {
    

    String userName;
    String foto;
    Integer posicion;
    Integer partidaId;

    public PartidaJugadorView(String userName, String foto, Integer posicion,Integer partidaId) {
        this.userName = userName;
        this.foto = foto;
        this.posicion=posicion;
        this.partidaId=partidaId;
    }
    
    
}
