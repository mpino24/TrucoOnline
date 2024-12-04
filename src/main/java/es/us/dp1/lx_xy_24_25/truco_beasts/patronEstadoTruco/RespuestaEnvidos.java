package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;


import java.util.Objects;

import org.jpatterns.gof.StatePattern;
import org.springframework.beans.factory.annotation.Autowired;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Mano;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;



@StatePattern.ConcreteState
public class RespuestaEnvidos extends RespuestaTruco{

    private final ManoService manoService; 
    private final Cantos tipoEnvido;
    @Autowired
    public RespuestaEnvidos(ManoService manoService, Cantos tipoEnvido){
        this.manoService = manoService;
        this.tipoEnvido = tipoEnvido;

    }

    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (o == null || getClass() != o.getClass()) return false; 
        RespuestaSubirTruco respuestaRetruco = (RespuestaSubirTruco) o; 
        return true; 
    }
            
    @Override public int hashCode() { 
        return Objects.hash(); 
        
    }

    @Override
    public Cantos getTipoRespuestaTruco() {
        return Cantos.ENVIDO;
    }

    @Override
    public Mano accionRespuestaTruco(Mano manoActual, Integer puntosTruco)  {
        Boolean puedeEnvido = manoActual.getPuedeCantarEnvido(); 
        
        String codigo = manoActual.getPartida().getCodigo();
        
        if(puedeEnvido){
            manoActual =manoService.cantosEnvido(codigo, tipoEnvido);
        }else{
            throw new TrucoException( "Respuesta al truco no valida"); 
        }
        
        
        return manoActual;
    }
    
}
