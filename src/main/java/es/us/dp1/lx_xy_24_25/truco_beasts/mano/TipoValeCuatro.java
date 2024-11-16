package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

public class TipoValeCuatro extends Truco{

    

    @Override
    public CantosTruco getTipoTruco(){
        return CantosTruco.VALECUATRO;
    }

    @Override
    public void accionAlTipoTruco(Mano manoActual,Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos,
            List<Integer> listaRondaJugador, Integer rondaActual) {
        List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
        Integer elQueResponde = ManoService.quienResponde(cantoEnRetruco, jugadorTurno);
        manoActual.setJugadorTurno(elQueResponde);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
    }
    
}
