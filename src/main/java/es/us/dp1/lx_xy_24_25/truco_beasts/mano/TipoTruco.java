package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;



public class TipoTruco extends Truco{


    @Override
    public CantosTruco getTipoTruco(){
        return CantosTruco.TRUCO;
    }

    @Override
    public void accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos, List<Integer> listaRondaJugador, Integer rondaActual ) {
        listaRondaJugador.add(rondaActual);
        listaRondaJugador.add(jugadorTurno);
        manoActual.setEquipoCantor(getEquipo(jugadorTurno));//el 0 es el equipo 1 (los pares) y el 1 es el equipo 2 (impares) 
                                                             //se le podría sumar 1 al resultado del modulo y quedan con el mismo numero (yo creo que lo complica más) 
                
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
    }

    //TODO: FALTA TEST (NO SE NI SI ES NECESARIA ESTA FUNCIÓN)

    public Integer getEquipo(Integer jugador){ // Podria devolver un integer sino
        Integer equipo = null;
        if (jugador%2==0) equipo=0; //equipo 1
        else if(jugador%2==1) equipo =1; //equipo 2
        return equipo;
    }
    
}
