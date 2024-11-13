package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.List;

public class RespuestaQuieroTruco extends RespuestaTruco{



    @Override
    public RespuestasTruco getTipoRespuestaTruco() {
        return RespuestasTruco.QUIERO;
    }

    @Override
    public void accionRespuestaTruco(Mano manoActual,Integer jugadorTurno,Integer jugadorAnterior, Integer truco, List<List<Integer>> secuenciaCantos,Integer queTrucoEs) {
        manoActual.setPuntosTruco(truco +1);
        if(queTrucoEs == 1){ //Es decir, Truco
                manoActual.setJugadorTurno(jugadorAnterior);
        } else if( queTrucoEs == 2){
                List<Integer> cantoEnTruco = secuenciaCantos.get(0);
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                Integer aQuienLeTocaAhora = ManoService.aQuienLeToca(cantoEnTruco, cantoEnRetruco, jugadorTurno);
                manoActual.setJugadorTurno(aQuienLeTocaAhora);
                    
        } else {         
                List<Integer> cantoEnRetruco = secuenciaCantos.get(1);
                List<Integer> cantoEnValecuatro = secuenciaCantos.get(2);
                Integer aQuienLeTocaAhora = ManoService.aQuienLeToca(cantoEnRetruco, cantoEnValecuatro, jugadorTurno);
                manoActual.setJugadorTurno(aQuienLeTocaAhora);
        }  
    } 
    
    
}
