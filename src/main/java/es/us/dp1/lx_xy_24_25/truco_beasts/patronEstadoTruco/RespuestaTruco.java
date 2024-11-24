package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;

@StatePattern.Context
public abstract class RespuestaTruco {
    public abstract RespuestasTruco getTipoRespuestaTruco();
    public abstract Mano accionRespuestaTruco(Mano manoActual, Integer jugadorTurno,Integer jugadorAnterior, Integer truco, List<List<Integer>> secuenciaCantos,Integer queTrucoEs) throws Exception;
    
}
