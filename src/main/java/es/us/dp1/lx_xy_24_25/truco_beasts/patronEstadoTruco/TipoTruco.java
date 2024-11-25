package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;


@StatePattern.ConcreteState
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
    public CantosTruco getTipoTruco(){
        return CantosTruco.TRUCO;
    }

    @Override
    public Mano accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos, List<Integer> listaRondaJugador, Integer rondaActual,ManoService manoService, String codigo ) {
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);
        manoActual.setEquipoCantor(getEquipo(jugadorTurno));//el 0 es el equipo 1 (los pares) y el 1 es el equipo 2 (impares) 
                                                             //se le podría sumar 1 al resultado del modulo y quedan con el mismo numero (yo creo que lo complica más) 
        manoActual.setJugadorTurno(manoService.siguienteJugador(jugadorTurno));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
        return manoActual;
    }

    //TODO: FALTA TEST (NO SE NI SI ES NECESARIA ESTA FUNCIÓN)

    public Integer getEquipo(Integer jugador){ // Podria devolver un integer sino
        Integer equipo = null;
        if (jugador%2==0) equipo=0; //equipo 1
        else if(jugador%2==1) equipo =1; //equipo 2
        return equipo;
    }
    
}
