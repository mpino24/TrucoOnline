package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
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
    public Cantos getTipoRespuestaTruco() {
        return Cantos.NO_QUIERO;
    }
    
    @Override  
    public Mano accionRespuestaTruco(Mano manoActual, Integer puntosTruco) {

        manoActual.setPuntosTruco(puntosTruco);
        List<Integer> parche = new ArrayList<>();
        parche.add(0);
        parche.add(0);
        Integer equipo= manoActual.getJugadorTurno() %2;
        if(equipo==1){
            parche.set(0, 3);
        }else{
            parche.set(1, 3);
        }
        manoActual.setGanadoresRondas(parche);
        manoActual.setTerminada(true);
        Integer jugadorActual = manoActual.getJugadorTurno();
        List<Integer> listaE1= List.of(0, 3);
        List<Integer> listaE2= List.of(3, 0);
        if(jugadorActual%2==0){
            manoActual.setGanadoresRondas(listaE1);
        }else{
            manoActual.setGanadoresRondas(listaE2);
        }
        
        manoActual.setUltimoMensaje(getTipoRespuestaTruco());
        return manoActual;
    }
    
}
