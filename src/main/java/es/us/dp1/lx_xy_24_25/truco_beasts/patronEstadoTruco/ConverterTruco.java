package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.TipoRetruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.TipoTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.TipoValeCuatro;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Truco;
import jakarta.persistence.AttributeConverter;

public class ConverterTruco implements AttributeConverter<Truco, CantosTruco>{

    @Override
    public CantosTruco convertToDatabaseColumn(Truco attribute) {
        return attribute.getTipoTruco();
    }

    @Override
    public Truco convertToEntityAttribute(CantosTruco dbData) {
        if(dbData.equals(CantosTruco.TRUCO)){
            return new TipoTruco();
        }else if (dbData.equals(CantosTruco.RETRUCO)){
            return new TipoRetruco();
        }else if (dbData.equals(CantosTruco.VALECUATRO)){
            return new TipoValeCuatro();
        }else{
            throw new IllegalArgumentException("Tipo de canto no valido:"+ dbData);
        }
    }



}