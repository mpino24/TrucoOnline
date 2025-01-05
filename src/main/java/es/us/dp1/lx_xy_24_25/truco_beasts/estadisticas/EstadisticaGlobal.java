package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import lombok.Getter;
import lombok.Setter;
import org.jpatterns.gof.CompositePattern.Component;

@Component
@Getter
@Setter
public class EstadisticaGlobal extends EstadisticaJugador {

    public Integer jugadoresTotales=0;
    public Integer partidasConFlor=0;


    
}
