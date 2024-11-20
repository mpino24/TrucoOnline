package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

public abstract class Truco {
    public abstract CantosTruco getTipoTruco();
    public abstract void accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos, List<Integer> listaRondaJugador, Integer rondaActual,ManoService manoService );
    

}
