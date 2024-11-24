package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import java.util.List;
import java.util.Objects;

import org.jpatterns.gof.StatePattern;


import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;



@StatePattern.ConcreteState
public class RespuestaSubirTruco extends RespuestaTruco{

        @Override 
        public boolean equals(Object o) { 
            if (this == o) return true; 
            if (o == null || getClass() != o.getClass()) return false; 
            RespuestaSubirTruco respuestaSubirTruco = (RespuestaSubirTruco) o; 
            return true; 
        }
            
        @Override public int hashCode() { 
            return Objects.hash(); 
        
        }

    @Override
    public RespuestasTruco getTipoRespuestaTruco() {
        return RespuestasTruco.SUBIR;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual, Integer jugadorTurno, Integer jugadorAnterior, Integer truco,
            List<List<Integer>> secuenciaCantos, Integer queTrucoEs) throws Exception {
        if(truco == 1){
                manoActual.setPuntosTruco(truco+1); //Declaramos como un "quiero" el truco
                manoActual =manoActual.cantosTruco(CantosTruco.RETRUCO);
        }else if(truco==2){
                manoActual.setPuntosTruco(truco +1);
                manoActual= manoActual.cantosTruco(CantosTruco.VALECUATRO);
        } else {
                throw new Exception( "No se puede subir más, capo"); //GESTIONAR MEJOR
        }
        return manoActual;
    }
    
}
