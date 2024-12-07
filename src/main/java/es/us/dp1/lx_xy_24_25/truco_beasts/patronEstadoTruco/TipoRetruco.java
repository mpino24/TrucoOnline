package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;



@StatePattern.ConcreteState
public class TipoRetruco extends Truco{


    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        TipoRetruco TipoRetruco = (TipoRetruco) o; 
        return true; 
    }
        
    @Override public int hashCode() { 
        return Objects.hash(); 
    
    }

    @Override
    public Cantos getTipoTruco(){
        return Cantos.RETRUCO;
    }

    @Override
    public Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor) {
        Integer puntosTruco = manoActual.getPuntosTruco();
        Integer puntosHayTruco = 2;
        if (puntosTruco < puntosHayTruco) {
                throw new TrucoException( "No se cantó el truco");
            } else if(puntosTruco>puntosHayTruco){
                throw new TrucoException("Ya se canto el retruco");
            }
        Integer elQueRespondeAlRetruco = manoActual.quienResponde();
        manoActual.setJugadorTurno(elQueRespondeAlRetruco);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        
        
        return manoActual;
    }


}
