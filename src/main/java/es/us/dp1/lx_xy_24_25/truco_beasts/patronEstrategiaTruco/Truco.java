package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;



import org.jpatterns.gof.StrategyPattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;


@StrategyPattern.Context
public abstract class Truco {
    public abstract Cantos getTipoTruco();
    public abstract Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor);
    

}
