package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorVictorias {
    String username;
    Long victorias;

    public JugadorVictorias(String username, Long victorias){
        this.username=username;
        this.victorias=victorias;
    }
}
