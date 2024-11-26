package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;



@StatePattern.ConcreteState
public class RespuestaNoQuieroTruco extends RespuestaTruco{

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        RespuestaNoQuieroTruco respuestaNoQuieroTruco = (RespuestaNoQuieroTruco) o; 
        return true; 
    }
        
    @Override public int hashCode() { 
        return Objects.hash(); 
    
    }

    @Override
    public RespuestasTruco getTipoRespuestaTruco() {
        return RespuestasTruco.NO_QUIERO;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual, Integer jugadorTurno, Integer jugadorAnterior, Integer truco,
            List<List<Integer>> secuenciaCantos, Integer queTrucoEs) {
        
        manoActual.setPuntosTruco(truco);
        return manoActual;
    }
    
}
