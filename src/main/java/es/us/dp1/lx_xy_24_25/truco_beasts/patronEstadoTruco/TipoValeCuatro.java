package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;
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
    public CantosTruco getTipoTruco(){
        return CantosTruco.VALECUATRO;
    }

    @Override
    public void accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos,
            List<Integer> listaRondaJugador, Integer rondaActual, ManoService manoService) {
        List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
        Integer elQueResponde = manoService.quienResponde(cantoEnRetruco, jugadorTurno);
        manoActual.setJugadorTurno(elQueResponde);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
    }
    
}
