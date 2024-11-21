package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;


import es.us.dp1.lx_xy_24_25.truco_beasts.mano.PatronEstadoTruco.ConverterRespuestaTruco;

public class TestConverterRespuestaTruco {

    ConverterRespuestaTruco convertidorRespuestaTruco;

    
    @Before
    public void setUp() {
        convertidorRespuestaTruco = new ConverterRespuestaTruco(); 
    } 
    
    @Test
    public void devuelveTipoRespuestaQuieroTruco(){
        RespuestaTruco respuestaTruco= new RespuestaQuieroTruco();
        RespuestasTruco resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(RespuestasTruco.QUIERO, resultado);
    }

    @Test
    public void devuelveTipoRespuestaNoQuieroTruco(){
        RespuestaTruco respuestaTruco= new RespuestaNoQuieroTruco();
        RespuestasTruco resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(RespuestasTruco.NO_QUIERO, resultado);
    }

    @Test
    public void devuelveTipoRespuestaSubirTruco(){
        RespuestaTruco respuestaTruco= new RespuestaSubirTruco();
        RespuestasTruco resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(RespuestasTruco.SUBIR, resultado);
    }

    
    
    @Test public void devuelveEntidadTipoRespuestaQuieroTruco() { 
        RespuestasTruco respuesta = RespuestasTruco.QUIERO; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaQuieroTruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoRespuestaNoQuieroTruco() { 
        RespuestasTruco respuesta = RespuestasTruco.NO_QUIERO; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaNoQuieroTruco(), resultado);  
    } 
    
    @Test public void devuelveEntidadTipoRespuestaSubirTruco() { 
        RespuestasTruco respuesta = RespuestasTruco.SUBIR; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaSubirTruco(), resultado); 
    } 


    

    
}
