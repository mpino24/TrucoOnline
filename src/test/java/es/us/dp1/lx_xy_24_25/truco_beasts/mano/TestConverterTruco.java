package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;

import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.ConverterTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.TipoRetruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.TipoTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.TipoValeCuatro;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstrategiaTruco.Truco;

public class TestConverterTruco {

    ConverterTruco convertidorTruco;

    
    @Before
    public void setUp() {
        convertidorTruco = new ConverterTruco(); 
    } 
    
    @Test
    public void devuelveTipoTruco(){
        Truco tipoTruco= new TipoTruco();
        Cantos resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(Cantos.TRUCO, resultado);
    }

    @Test
    public void devuelveTipoRetruco(){
        Truco tipoTruco= new TipoRetruco();
        Cantos resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(Cantos.RETRUCO, resultado);
    }

    @Test
    public void devuelveTipoValeCuatro(){
        Truco tipoTruco= new TipoValeCuatro();
        Cantos resultado = convertidorTruco.convertToDatabaseColumn(tipoTruco);
        assertEquals(Cantos.VALECUATRO, resultado);
    }

    
    
    @Test public void devuelveEntidadTipoTruco() { 
        Cantos canto = Cantos.TRUCO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoTruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoRetruco() { 
        Cantos canto = Cantos.RETRUCO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoRetruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoValeCuatro() { 
        Cantos canto = Cantos.VALECUATRO; 
        Truco resultado = convertidorTruco.convertToEntityAttribute(canto); 
        assertEquals(new TipoValeCuatro(), resultado); 
    } 
    
}
