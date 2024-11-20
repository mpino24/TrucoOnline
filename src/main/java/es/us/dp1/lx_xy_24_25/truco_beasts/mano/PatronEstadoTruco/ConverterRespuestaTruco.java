package es.us.dp1.lx_xy_24_25.truco_beasts.mano.PatronEstadoTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.RespuestaNoQuieroTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.RespuestaQuieroTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.RespuestaSubirTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.RespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.RespuestasTruco;

import jakarta.persistence.AttributeConverter;

public class ConverterRespuestaTruco implements AttributeConverter<RespuestaTruco, RespuestasTruco> {

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
            return new RespuestaSubirTruco();
        }
    }
    
}
