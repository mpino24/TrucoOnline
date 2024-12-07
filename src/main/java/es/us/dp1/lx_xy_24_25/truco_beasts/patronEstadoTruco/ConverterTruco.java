package es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco;

import org.jpatterns.gof.CompositePattern.Component;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Cantos;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;

@Component
@Convert
public class ConverterTruco implements AttributeConverter<Truco, Cantos>{

    @Override
    public Cantos convertToDatabaseColumn(Truco attribute) {
        return attribute.getTipoTruco();
    }

    @Override
    public Truco convertToEntityAttribute(Cantos dbData) {
        if(dbData.equals(Cantos.TRUCO)){
            return new TipoTruco();
        }else if (dbData.equals(Cantos.RETRUCO)){
            return new TipoRetruco();
        }else if (dbData.equals(Cantos.VALECUATRO)){
            return new TipoValeCuatro();
        } else{
            throw new TrucoException( "Canto no valido");
        }
    }
    

    
}
