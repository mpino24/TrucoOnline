package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;


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
                manoActual.setJugadorTurno(jugadorAnterior);
        } else if( queTrucoEs == 2){
                List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer aQuienLeTocaAhora = manoActual.aQuienLeToca(cantoEnTruco, cantoEnRetruco, jugadorTurno);
                manoActual.setJugadorTurno(aQuienLeTocaAhora);
                    
        } else {         
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                Integer aQuienLeTocaAhora = manoActual.aQuienLeToca(cantoEnRetruco, cantoEnValecuatro, jugadorTurno);
                manoActual.setJugadorTurno(aQuienLeTocaAhora);
        }
        return manoActual;
    } 
    
    
}
