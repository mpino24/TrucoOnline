package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
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
        Integer puntosTruco = manoActual.getPuntosTruco();
        Integer puntosHayRetruco = 3;
        if (puntosTruco < puntosHayRetruco) {
            throw new TrucoException( "No se cantó el retruco"); 
        }else if(puntosTruco > puntosHayRetruco){
            throw new TrucoException("Ya se canto el valecuatro");
        }
        Integer elQueResponde = manoActual.quienResponde();
        manoActual.setJugadorTurno(elQueResponde);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        
        
        return manoActual;
    }
    
}
