package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;


import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;


@StatePattern.Context
public abstract class Truco {
    public abstract Cantos getTipoTruco();
    public abstract Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor);
    

}
