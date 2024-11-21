package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Before;
import org.junit.Test;


import es.us.dp1.lx_xy_24_25.truco_beasts.mano.PatronEstadoTruco.ConverterTruco;

public class TestConverterTruco {

    ConverterTruco convertidorTruco;

    
    @Before
    public void setUp() {
        convertidorTruco = new ConverterTruco(); 
    } 
    
    @Test
    public void devuelveTipoTruco(){
        Truco tipoTruco= new TipoTruco();
        CantosTruco resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(CantosTruco.TRUCO, resultado);
    }

    @Test
    public void devuelveTipoRetruco(){
        Truco tipoTruco= new TipoRetruco();
        CantosTruco resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(CantosTruco.RETRUCO, resultado);
    }

    @Test
    public void devuelveTipoValeCuatro(){
        Truco tipoTruco= new TipoValeCuatro();
        CantosTruco resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(CantosTruco.VALECUATRO, resultado);
    }

    
    
    @Test public void devuelveEntidadTipoTruco() { 
        CantosTruco canto = CantosTruco.TRUCO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoTruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoRetruco() { 
        CantosTruco canto = CantosTruco.RETRUCO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoRetruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoValeCuatro() { 
        CantosTruco canto = CantosTruco.VALECUATRO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoValeCuatro(), resultado); 
    } 
    
}
