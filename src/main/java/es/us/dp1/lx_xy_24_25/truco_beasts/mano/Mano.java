package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mano {
    
    private Integer jugadorMano;
    private List<List<Carta>> cartasDisp; // La lista de cartas de la posicion 0 seran las cartas que tiene el jugador en la posicion 0

    @ManyToOne
    private Partida partida;
}
