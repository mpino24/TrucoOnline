package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;




public class TestManoService {



    Partida partida = new Partida();
    Mano mano = new Mano();
    String codigo = null;
    
    ManoService manoService = null;

   
    CartaRepository cartaRepository;
    PartidaService partidaService;


    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTS");
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);

        Integer tiposDeEnvido = 3;
        Integer envidosIniciales=0;
        List<Integer> envidos = new ArrayList<>();
        for(int i = 0; i<tiposDeEnvido;i++){
            envidos.add(envidosIniciales);
        }
        mano.setEnvidosCantados(envidos);
        

        manoService = new ManoService(cartaRepository, partidaService);
        codigo = partida.getCodigo();
        manoService.actualizarMano(mano, codigo);
        
    }
    

    @Test
    public void testRepartirCartas() {
       setup(0,4);
            
        CartaRepository cartaRepository = Mockito.mock(CartaRepository.class);
        Carta carta = mock(Carta.class);

        when(cartaRepository.findById(anyInt())).thenReturn(Optional.of(carta));
        
        manoService = new ManoService(cartaRepository, partidaService);
    
        List<List<Carta>> cartasRepartidas = manoService.repartirCartas(partida);
    
            
        assertNotNull(cartasRepartidas);
        assertEquals(4, cartasRepartidas.size()); // 4 jugadores
        cartasRepartidas.forEach(cartas -> assertEquals(3, cartas.size())); // Cada jugador recibe 3 cartas
    
    }

    public void setupCartasDisponibles(Integer jugadorTurno, Integer ronda) {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
        c0.setId(0);
        c1.setId(1);
        c2.setId(2);
        c0.setPoder(14);
        c1.setPoder(13);
        c2.setPoder(6);
        mano.setJugadorTurno(jugadorTurno);
        List<Carta> listaBase = new ArrayList<>();
        
        List<List<Carta>> cartasDisponibles = new ArrayList<>();
        List<Carta> cartasRonda = new ArrayList<>();
        Integer numJugadores = partida.getNumJugadores();
        if(ronda ==1){
            listaBase.add(c0); listaBase.add(c1); listaBase.add(c2);
            
        } else if(ronda ==2){
            listaBase.add(c0);listaBase.add(c1); 
            
        } else{
            listaBase.add(c0);
            
        }
        mano.setRondaActual(ronda);

        for(int i = 0;i <numJugadores; i++){
            cartasRonda.add(null);
            cartasDisponibles.add(listaBase);
        }
        mano.setCartasDisp(cartasDisponibles);
        mano.setCartasLanzadasRonda(cartasRonda);
    }

    @Test
    public void tirarCartaSePoneANullCartaDisponible()  {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Carta cartaLanzada = mano.getCartasDisp().get(jugadorActual).get(cartaIdALanzar);
        assertEquals(null, cartaLanzada);
        
    }
    @Test
    public void tirarCartaHayCartaLanzada() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
        
    }
    @Test
    public void tirarCartaSeAvanzaDeTurno() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Integer siguienteTurno = mano.getJugadorTurno();
        assertEquals(mano.siguienteJugador(jugadorActual), siguienteTurno);
    }

    @Test 
    public void tirarCartaNoDisponible() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        CartaTiradaException cartaTiradaException=assertThrows(CartaTiradaException.class,()-> manoService.tirarCarta(codigo, cartaIdALanzar));
        assertEquals("Esa carta ya fue tirada, no podés volver a hacerlo", cartaTiradaException.getMessage());
    }
    @Test 
    public void tirarCartaTieneQueResponder() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);
        mano.setEsperandoRespuesta(true);
       
        CartaTiradaException cartaTiradaException =assertThrows(CartaTiradaException.class,()-> manoService.tirarCarta(codigo, cartaIdALanzar));
        assertEquals("Tenés que responder antes de poder tirar una carta", cartaTiradaException.getMessage());
    }
    @Test
    public void tirarCartaCambiaDeRonda() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,2);
        setupCartasDisponibles(jugadorActual,1);
        manoService.tirarCarta(codigo, cartaIdALanzar);
        cartaIdALanzar = 1;
        manoService.tirarCarta(codigo, cartaIdALanzar);
        assertEquals(2, mano.getRondaActual());
    }

    public void setupTruco(Integer equipoCantor, Integer truco, Integer jugadorIniciadorCanto){
        if(equipoCantor!=null) mano.setEquipoCantor(equipoCantor);
        if(truco!=null) mano.setPuntosTruco(truco);

        if(jugadorIniciadorCanto !=null){ // De ponerlo en otro valor que no sea null, quiere decir que estamos en un "ida y vuelta" de cantos
            mano.setJugadorIniciadorDelCanto(jugadorIniciadorCanto); 
        }
    }
    
    @Test 
    public void cantaTruco() {
        setup(0, 4);
        setupTruco(null, null, null);
        setupCartasDisponibles(0, 1);
        
        manoService.cantosTruco(codigo,CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
       
    }

    

    @Test 
    public void cantaRetruco() {
        setup(0, 4);
        setupTruco(1, 2,null);
        setupCartasDisponibles(0, 2);
        
        
        
        manoService.cantosTruco(codigo,CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
        
    }

    @Test 
    public void cantaValecuatro() {
        setup(0, 4);
        setupTruco(1, 3,null);
        setupCartasDisponibles(0, 2);
        
        
        manoService.cantosTruco(codigo,CantosTruco.VALECUATRO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
        
    }

    @Test
    public void responderQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        
        manoService.cantosTruco(codigo,CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO);
        assertTrue(mano.getJugadorTurno() ==0);
        assertEquals(2, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        
        manoService.cantosTruco(codigo,CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        manoService.responderTruco(codigo,RespuestasTruco.NO_QUIERO);
        assertEquals(1, mano.getPuntosTruco());
        
    }

    @Test
    public void responderQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2,null); //Se canto Truco
        setupCartasDisponibles(1, 1);
        
        
       
        manoService.cantosTruco(codigo,CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2);

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(3, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2,null); //Se canto Truco
        setupCartasDisponibles(1, 1);
        
        
        manoService.cantosTruco(codigo,CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2); 

        manoService.responderTruco(codigo,RespuestasTruco.NO_QUIERO); 
        assertEquals(2, mano.getPuntosTruco());
        
    }

    @Test
    public void responderQuieroValecuatro() {
        setup(0, 4);
        setupTruco(0, 3,0); //el jugador anterior le canto Retruco 
        setupCartasDisponibles(1, 2);

    
        
        
        manoService.cantosTruco(codigo,CantosTruco.VALECUATRO);
        assertEquals(2, mano.getJugadorTurno()); 
        assertTrue(mano.getEsperandoRespuesta());

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
            
        assertFalse(mano.getEsperandoRespuesta());
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(4, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroValecuatro(){
        setup(0, 4);
        setupTruco(0, 3,0); //el jugador anterior le canto Retruco 
        setupCartasDisponibles(1, 2);
        
        
        manoService.cantosTruco(codigo,CantosTruco.VALECUATRO);

        assertEquals(2,mano.getJugadorTurno()); 


        manoService.responderTruco(codigo,RespuestasTruco.NO_QUIERO);
        assertEquals(3, mano.getPuntosTruco());
        
    }


    @Test
    public void responderTrucoRetrucoQuiero() {
        setup(0, 4);
        setupTruco(null, null, null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
       
        manoService.cantosTruco(codigo,CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() == 1);

        manoService.responderTruco(codigo,RespuestasTruco.SUBIR); 
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
        
    }

    @Test
    public void responderRetrucoValeCuatroQuiero() {
        setup(0, 4);
        setupTruco(0, 2,null); //Se canto Truco
        setupCartasDisponibles(3, 2);
        
        
        
        manoService.cantosTruco(codigo,CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 0);

        manoService.responderTruco(codigo,RespuestasTruco.SUBIR); 
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());
        
    }


    @Test 
    public void responderTrucoRetrucoValecuatroQuiero() {
        setup(0, 4);
        setupTruco(null, null,null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
        
        manoService.cantosTruco(codigo,CantosTruco.TRUCO); 
        assertEquals(1,mano.getJugadorTurno());

        manoService.responderTruco(codigo,RespuestasTruco.SUBIR);
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        manoService.responderTruco(codigo,RespuestasTruco.SUBIR);
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(1, mano.getJugadorTurno());

        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
        
    }

    @Test 
    public void cantarTrucoNoPuede() {
        setup(0, 4);
        setupTruco(null, null,null); //No se canto nada
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo,CantosTruco.TRUCO); 
        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        
        TrucoException exception = assertThrows(TrucoException.class, 
        () -> manoService.cantosTruco(codigo,CantosTruco.TRUCO));
    
        
        assertEquals("No podes cantar truco ni ninguna de sus variantes", exception.getMessage());
    }
    @Test 
    public void cantarTrucoYaSeCanto() {
        setup(0, 4);
        setupTruco(null, null,null); //No se canto nada
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo,CantosTruco.TRUCO); 
        manoService.responderTruco(codigo,RespuestasTruco.QUIERO); 
        mano.siguienteTurno();
        assertTrue(mano.comprobarSiPuedeCantarTruco()); 

        TrucoException exception = assertThrows(TrucoException.class, 
        () -> manoService.cantosTruco(codigo,CantosTruco.TRUCO));
    
        
        assertEquals("Ya se canto el truco", exception.getMessage());
        
    }
    @Test 
    public void cantarRetrucoNoSeCantoTruco() {
        setup(0, 4);
        setupTruco(null, null, null); //No se canto nada
        setupCartasDisponibles(0, 1);

    
        TrucoException exception = assertThrows(TrucoException.class, 
        () -> manoService.cantosTruco(codigo,CantosTruco.RETRUCO));
    
        
        assertEquals("No se cantó el truco", exception.getMessage());
        
    }
}
