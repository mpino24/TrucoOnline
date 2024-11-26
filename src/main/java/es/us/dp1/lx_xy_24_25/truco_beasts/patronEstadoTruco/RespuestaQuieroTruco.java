package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

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
    public RespuestasTruco getTipoRespuestaTruco() {
        return RespuestasTruco.QUIERO;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual,Integer jugadorTurno,Integer jugadorAnterior, Integer truco, List<List<Integer>> secuenciaCantos,Integer queTrucoEs) {
        manoActual.setPuntosTruco(truco +1);
        if(queTrucoEs == 1){ //Es decir, Truco
                manoActual.setJugadorTurno(jugadorAnterior); //Bien
        } else if( queTrucoEs == 2){
                
                Integer aQuienLeTocaAhora = manoActual.aQuienLeToca();
                manoActual.setJugadorTurno(aQuienLeTocaAhora); //ver
                    
        } else {         
                
                Integer aQuienLeTocaAhora = manoActual.aQuienLeToca();
                manoActual.setJugadorTurno(aQuienLeTocaAhora); // ver
        }
        return manoActual;
    } 
    
    
}
