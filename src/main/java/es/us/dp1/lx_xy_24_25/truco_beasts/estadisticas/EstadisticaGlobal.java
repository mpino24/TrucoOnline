package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jpatterns.gof.CompositePattern.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class EstadisticaGlobal extends EstadisticaJugador{

    public Integer jugadoresTotales=0;
    public Integer partidasConFlor=0;
}
