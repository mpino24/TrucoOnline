package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;


@StatePattern.Context
public abstract class Truco {
    public abstract CantosTruco getTipoTruco();
    public abstract Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos, List<Integer> listaRondaJugador, Integer rondaActual);
    

}
