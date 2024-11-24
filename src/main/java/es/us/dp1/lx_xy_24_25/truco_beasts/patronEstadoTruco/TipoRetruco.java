package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;


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
    public CantosTruco getTipoTruco(){
        return CantosTruco.RETRUCO;
    }

    @Override
    public Mano accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos,
            List<Integer> listaRondaJugador, Integer rondaActual,ManoService manoService, String codigo) {
        List<Integer> cantoEnTruco = secuenciaCantos.get(0);
        Integer elQueRespondeAlRetruco = manoService.quienResponde(cantoEnTruco, jugadorTurno,codigo);
        manoActual.setJugadorTurno(elQueRespondeAlRetruco);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
        return manoActual;
    }


}
