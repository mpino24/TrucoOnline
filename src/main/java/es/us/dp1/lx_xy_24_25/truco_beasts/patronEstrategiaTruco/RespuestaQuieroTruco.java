package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;



import org.jpatterns.gof.StrategyPattern;
@StrategyPattern.ConcreteStrategy
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
    public Mano accionRespuestaTruco(Mano manoActual, Integer puntosTruco) {
        Integer maximoPuntajeTruco=4;
        
        if(puntosTruco +1 > maximoPuntajeTruco){
            manoActual.setPuntosTruco(maximoPuntajeTruco);
            throw new TrucoException("El máximo puntaje obtenible en el truco son " + maximoPuntajeTruco +" puntos");
        }
        manoActual.setPuntosTruco(puntosTruco +1);
        manoActual.setEsperandoRespuesta(false);
        manoActual.setJugadorTurno(manoActual.getJugadorIniciadorDelCanto());
        
        manoActual.setUltimoMensaje(getTipoRespuestaTruco());
        manoActual.comprobarSiPuedeCantarEnvido(false);
        return manoActual;
    } 
    
    
}
