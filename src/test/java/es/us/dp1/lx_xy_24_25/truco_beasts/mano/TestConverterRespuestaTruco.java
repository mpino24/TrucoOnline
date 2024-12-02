package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.ConverterRespuestaTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaNoQuieroTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaQuieroTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaSubirTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestaTruco;

public class TestConverterRespuestaTruco {

    ConverterRespuestaTruco convertidorRespuestaTruco;
    
    @Autowired
    ManoService manoService ;
    
    @Before
    public void setUp() {
        convertidorRespuestaTruco = new ConverterRespuestaTruco(manoService); 
    } 
    
    @Test
    public void devuelveTipoRespuestaQuieroTruco(){
        RespuestaTruco respuestaTruco= new RespuestaQuieroTruco();
        Cantos resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(Cantos.QUIERO, resultado);
    }

    @Test
    public void devuelveTipoRespuestaNoQuieroTruco(){
        RespuestaTruco respuestaTruco= new RespuestaNoQuieroTruco();
        Cantos resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(Cantos.NO_QUIERO, resultado);
    }

    @Test
    public void devuelveTipoRespuestaSubirTruco(){
        RespuestaTruco respuestaTruco= new RespuestaSubirTruco(manoService);
        Cantos resultado = convertidorRespuestaTruco.convertToDatabaseColumn(respuestaTruco);
        assertEquals(Cantos.SUBIR, resultado);
    }

    
    
    @Test public void devuelveEntidadTipoRespuestaQuieroTruco() { 
        Cantos respuesta = Cantos.QUIERO; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaQuieroTruco(), resultado); 
    } 
    
    @Test public void devuelveEntidadTipoRespuestaNoQuieroTruco() { 
        Cantos respuesta = Cantos.NO_QUIERO; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaNoQuieroTruco(), resultado);  
    } 
    
    @Test public void devuelveEntidadTipoRespuestaSubirTruco() { 
        Cantos respuesta = Cantos.SUBIR; 
        RespuestaTruco resultado = convertidorRespuestaTruco.convertToEntityAttribute(respuesta); 
        assertEquals(new RespuestaSubirTruco(manoService), resultado); 
    } 


    

    
}
