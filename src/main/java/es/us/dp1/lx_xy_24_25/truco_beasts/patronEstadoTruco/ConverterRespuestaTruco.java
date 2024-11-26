package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;
import org.jpatterns.gof.CompositePattern.Component;
import org.springframework.context.annotation.Lazy;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Component
@Convert
public class ConverterRespuestaTruco implements AttributeConverter<RespuestaTruco, RespuestasTruco> {

    public final ManoService manoService;

    public ConverterRespuestaTruco(@Lazy ManoService manoService){
        this.manoService = manoService;
    }


    @Override
    public RespuestasTruco convertToDatabaseColumn(RespuestaTruco attribute) {
        return attribute.getTipoRespuestaTruco();
    }

    @Override
    public RespuestaTruco convertToEntityAttribute(RespuestasTruco dbData) {
        if(dbData.equals(RespuestasTruco.QUIERO)){
            return new RespuestaQuieroTruco();
        }else if (dbData.equals(RespuestasTruco.NO_QUIERO)){
            return new RespuestaNoQuieroTruco();
        }else{
            return new RespuestaSubirTruco(manoService);
        }
    }
    
}
