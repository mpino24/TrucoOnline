package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;



import org.jpatterns.gof.StrategyPattern;


import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;


@StrategyPattern.Context
public abstract class RespuestaTruco {
    public abstract Cantos getTipoRespuestaTruco();
    public abstract Mano accionRespuestaTruco(Mano manoActual,Integer puntosTruco) ;
    
}
