package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;

@StatePattern.ConcreteState
public class TipoValeCuatro extends Truco{

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        TipoValeCuatro TipoValeCuatro = (TipoValeCuatro) o; 
        return true; 
    }
        
    @Override public int hashCode() { 
        return Objects.hash(); 
    
    }

    @Override
    public Cantos getTipoTruco(){
        return Cantos.VALECUATRO;
    }

    @Override
    public Mano accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor) {
        
        Integer elQueResponde = manoActual.quienResponde();
        manoActual.setJugadorTurno(elQueResponde);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        
        
        return manoActual;
    }
    
}
