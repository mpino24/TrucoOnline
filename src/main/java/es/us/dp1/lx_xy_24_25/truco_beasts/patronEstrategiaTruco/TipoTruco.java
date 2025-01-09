package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;


import org.jpatterns.gof.StrategyPattern;
@StrategyPattern.ConcreteStrategy
public class TipoTruco extends Truco{

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        TipoTruco tipoTruco = (TipoTruco) o; 
        return true; 
    }
        
    @Override public int hashCode() { 
        return Objects.hash(); 
    
    }
    
    @Override
    public Cantos getTipoTruco(){
        return Cantos.TRUCO;
    }

    @Override
    public Mano accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor) {
        Integer puntosNoHayTruco = 1;
        if(manoActual.getPuntosTruco() > puntosNoHayTruco){
            throw new TrucoException("Ya se canto el truco");
        }
        manoActual.setEquipoCantor(getEquipo(jugadorTurno));//el 0 es el equipo 1 (los pares) y el 1 es el equipo 2 (impares)                
        manoActual.setJugadorTurno(manoActual.siguienteJugador(jugadorTurno));
        manoActual.comprobarSiPuedeCantarFlor();

        
        return manoActual;
    }

    //TODO: FALTA TEST

    public Integer getEquipo(Integer jugador){ 
        Integer equipo = null;
        if (jugador%2==0) equipo=0; //equipo 1
        else if(jugador%2==1) equipo =1; //equipo 2
        return equipo;
    }
    
}
