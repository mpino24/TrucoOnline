package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;



@StatePattern.ConcreteState
public class RespuestaQuieroTruco extends RespuestaTruco{

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        RespuestaQuieroTruco respuestaQuieroTruco = (RespuestaQuieroTruco) o; 
        return true; 
    }
        
    @Override public int hashCode() { 
        return Objects.hash(); 
    
    }

    @Override
    public Cantos getTipoRespuestaTruco() {
        return Cantos.QUIERO;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual,Integer jugadorTurno,Integer jugadorAnterior, Integer puntosTruco) {
        manoActual.setPuntosTruco(puntosTruco +1);
        manoActual.setEsperandoRespuesta(false);
        manoActual.setJugadorTurno(manoActual.getJugadorIniciadorDelCanto());
        return manoActual;
    } 
    
    
}
