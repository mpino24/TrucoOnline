package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
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
        }else {
            return new TipoValeCuatro();
        }
    }
    

    
}
