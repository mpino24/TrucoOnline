package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

public class RespuestaNoQuieroTruco extends RespuestaTruco{


    @Override
    public RespuestasTruco getTipoRespuestaTruco() {
        return RespuestasTruco.NO_QUIERO;
    }

    @Override
    public void accionRespuestaTruco(Mano manoActual, Integer jugadorTurno, Integer jugadorAnterior, Integer truco,
            List<List<Integer>> secuenciaCantos, Integer queTrucoEs, ManoService manoService) {
        manoActual.setPuntosTruco(truco);
    }
    
}
