package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco;
import org.jpatterns.gof.CompositePattern.Component;
import org.springframework.context.annotation.Lazy;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.ManoService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Component
@Convert
public class ConverterRespuestaTruco implements AttributeConverter<RespuestaTruco, Cantos> {

    public final ManoService manoService;

    public ConverterRespuestaTruco(@Lazy ManoService manoService){
        this.manoService = manoService;
    }


    @Override
    public Cantos convertToDatabaseColumn(RespuestaTruco attribute) {
        return attribute.getTipoRespuestaTruco();
    }

    @Override
    public RespuestaTruco convertToEntityAttribute(Cantos dbData) {
        if(dbData.equals(Cantos.QUIERO)){
            return new RespuestaQuieroTruco();
        }else if (dbData.equals(Cantos.NO_QUIERO)){
            return new RespuestaNoQuieroTruco();
        }else if (dbData.equals(Cantos.SUBIR)){
            return new RespuestaSubirTruco(manoService);
        } else if (dbData.equals(Cantos.ENVIDO) || dbData.equals(Cantos.REAL_ENVIDO) || dbData.equals(Cantos.FALTA_ENVIDO)) {
            return new RespuestaEnvidos(manoService, dbData);
        }{
            throw new TrucoException( "Respuesta al truco no valida"); 
        }
    }
    
}
