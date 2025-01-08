package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;

import java.util.Objects;

import org.jpatterns.gof.StatePattern;
import org.springframework.beans.factory.annotation.Autowired;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;



@StatePattern.ConcreteState
public class RespuestaSubirTruco extends RespuestaTruco{

    private final ManoService manoService; 
    
    @Autowired
    public RespuestaSubirTruco(ManoService manoService){
        this.manoService = manoService;

    }

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
    public Cantos getTipoRespuestaTruco() {
        return Cantos.SUBIR;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual, Integer puntosTruco)  {
        String codigo = manoActual.getPartida().getCodigo();
        if(puntosTruco == 1){
                manoActual.setPuntosTruco(puntosTruco+1); //Declaramos como un "quiero" el truco
                manoActual =manoService.cantosTruco(codigo,Cantos.RETRUCO);
                manoActual.setUltimoMensaje(Cantos.RETRUCO);
        }else if(puntosTruco==2){
                manoActual.setPuntosTruco(puntosTruco +1);
                manoActual= manoService.cantosTruco(codigo,Cantos.VALECUATRO);
                manoActual.setUltimoMensaje(Cantos.VALECUATRO);
        } else {
                throw new TrucoException( "No se puede subir más, capo"); 
        }
        return manoActual;
    }
    
}
