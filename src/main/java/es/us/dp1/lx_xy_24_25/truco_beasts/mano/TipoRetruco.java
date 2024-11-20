package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

public class TipoRetruco extends Truco{




    @Override
    public CantosTruco getTipoTruco(){
        return CantosTruco.RETRUCO;
    }

    @Override
    public void accionAlTipoTruco(Mano manoActual, Integer jugadorTurno, Integer equipoCantor, List<List<Integer>> secuenciaCantos,
            List<Integer> listaRondaJugador, Integer rondaActual,ManoService manoService) {
        List<Integer> cantoEnTruco = secuenciaCantos.get(0);
        Integer elQueRespondeAlRetruco = manoService.quienResponde(cantoEnTruco, jugadorTurno);
        manoActual.setJugadorTurno(elQueRespondeAlRetruco);
        manoActual.setEquipoCantor((equipoCantor==0 ? 1:0));
        secuenciaCantos.add(listaRondaJugador);
        manoActual.setSecuenciaCantoLista(secuenciaCantos);
    }


}
