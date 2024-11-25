package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;

import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;



public class TestManoService {



    Partida partida = new Partida();
    Mano mano = new Mano();
    String codigo = null;
    
    ManoService manoService = null;

   
    CartaRepository cartaRepository;


    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTS");
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);
        
        manoService = new ManoService(cartaRepository);
        codigo = partida.getCodigo();
        manoService.actualizarMano(mano, codigo);
        
    }

    public void setupCartasDisponibles(Integer jugadorTurno, Integer ronda) {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
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

        for(int i = 0;i <numJugadores; i++){
            cartasRonda.add(null);
            cartasDisponibles.add(listaBase);
        }
        mano.setCartasDisp(cartasDisponibles);
        mano.setCartasLanzadasRonda(cartasRonda);
    }

    @Test
    public void tirarCartaSeBorraCartaDisponible() {
        Integer jugadorActual = 0;
        Integer cartaALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(cartaALanzar, partida.getCodigo());
        Integer tamCartasDisponibles = mano.getCartasDisp().get(jugadorActual).size();
        assertEquals(2, tamCartasDisponibles);
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
        Integer siguienteTurno = mano.getJugadorTurno();
        assertEquals(manoService.siguienteJugador(jugadorActual), siguienteTurno);
    }
    @Test
    public void tirarCartaHayCartaLanzada() {
        Integer jugadorActual = 0;
        Integer cartaALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(cartaALanzar,partida.getCodigo());
        
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
        
    }
    @Test
    public void tirarCartaSeAvanzaDeTurno() {
        Integer jugadorActual = 0;
        Integer cartaALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(cartaALanzar,partida.getCodigo());
        Integer siguienteTurno = mano.getJugadorTurno();
        assertEquals(manoService.siguienteJugador(jugadorActual), siguienteTurno);
    }

    public void setupTruco(Integer equipoCantor, Integer truco){
        if(equipoCantor!=null) mano.setEquipoCantor(equipoCantor);
        if(truco!=null) mano.setPuntosTruco(truco);
    }

    @Test 
    public void puedeCantarTrucoNoSeCanto(){
        setup(0, 4);
        setupTruco(null, null);
        mano.setJugadorTurno(3);
        assertTrue(mano.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(0, 2);
        mano.setJugadorTurno(3);
        assertTrue(mano.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(1, 2);
        mano.setJugadorTurno(3);
        assertFalse(mano.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(1);
        assertTrue(mano.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(0);
        assertFalse(mano.puedeCantarTruco());
    }

    public void setupSecuenciaCantos(Integer jugadorCantorTruco, Integer rondaTruco, Integer jugadorCantorRetruco, Integer rondaRetruco, Integer jugadorCantorValecuatro, Integer rondaValecuatro){
        List<List<Integer>> secuenciaCantos = new ArrayList<>();
        List<Integer> listaRondaJugadorTruco = new ArrayList<>();
        listaRondaJugadorTruco.add(rondaTruco);
        listaRondaJugadorTruco.add(jugadorCantorTruco);
        secuenciaCantos.add(listaRondaJugadorTruco);
        if(rondaRetruco!=null && jugadorCantorRetruco!=null){
            List<Integer> listaRondaJugadorRetruco = new ArrayList<>();
            listaRondaJugadorRetruco.add(rondaRetruco);
            listaRondaJugadorRetruco.add(jugadorCantorRetruco);
            secuenciaCantos.add(listaRondaJugadorRetruco);
            if(rondaValecuatro!=null && jugadorCantorValecuatro!=null){
                List<Integer> listaRondaJugadorValecuatro = new ArrayList<>();
                listaRondaJugadorValecuatro.add(rondaValecuatro);
                listaRondaJugadorValecuatro.add(jugadorCantorValecuatro);
                secuenciaCantos.add(listaRondaJugadorValecuatro);
            }
        }
        mano.setSecuenciaCantoLista(secuenciaCantos);
    }

    
  

    @Test 
    public void cantaTruco() throws Exception{
        setup(0, 4);
        setupTruco(null, null);
        setupCartasDisponibles(0, 1);
        try {
            manoService.cantosTruco(CantosTruco.TRUCO,codigo);
            assertTrue(mano.getJugadorTurno() == 1);
            assertEquals(0, mano.getEquipoCantor());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    

    @Test 
    public void cantaRetruco() throws Exception{
        setup(0, 4);
        setupTruco(1, 2);
        setupCartasDisponibles(0, 2);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        
        try {
            manoService.cantosTruco(CantosTruco.RETRUCO,codigo);
            assertTrue(mano.getJugadorTurno() == 1);
            assertEquals(0, mano.getEquipoCantor());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test 
    public void cantaValecuatro() throws Exception{
        setup(0, 4);
        setupTruco(1, 3);
        setupCartasDisponibles(0, 2);
        setupSecuenciaCantos(0,1, 1, 1, null, null);
        

        try {
            manoService.cantosTruco(CantosTruco.VALECUATRO,codigo);
            assertTrue(mano.getJugadorTurno() == 1);
            assertEquals(0, mano.getEquipoCantor());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderQuieroTruco() throws Exception{
        setup(0, 4);
        setupTruco(null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        try {
            manoService.cantosTruco(CantosTruco.TRUCO,codigo);
            assertTrue(mano.getJugadorTurno() ==1);

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo);
            assertTrue(mano.getJugadorTurno() ==0);
            assertEquals(2, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderNoQuieroTruco() throws Exception{
        setup(0, 4);
        setupTruco(null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        try {
            manoService.cantosTruco(CantosTruco.TRUCO,codigo);
            assertTrue(mano.getJugadorTurno() ==1);

            manoService.responderTruco(RespuestasTruco.NO_QUIERO,codigo);
            assertEquals(1, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderQuieroRetruco() throws Exception{
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(1, 1);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        
        try {
            manoService.cantosTruco(CantosTruco.RETRUCO,codigo);
            assertTrue(mano.getJugadorTurno() ==2);

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo); //QUIERO
            assertTrue(mano.getJugadorTurno() ==1);
            assertEquals(3, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderNoQuieroRetruco() throws Exception{
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(1, 1);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        try {
            manoService.cantosTruco(CantosTruco.RETRUCO,codigo);
            assertTrue(mano.getJugadorTurno() ==2); 

            manoService.responderTruco(RespuestasTruco.NO_QUIERO,codigo); //NO QUIERO
            assertEquals(2, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    @Test
    public void responderQuieroValecuatro() throws Exception{
        setup(0, 4);
        setupTruco(0, 3); //Se canto Retruco
        setupCartasDisponibles(1, 2);
        setupSecuenciaCantos(1, 1, 0, 1, null, null);
        
        try {
            manoService.cantosTruco(CantosTruco.VALECUATRO,codigo);
            assertEquals(2, mano.getJugadorTurno());
            assertTrue(mano.getEsperandoRespuesta());

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo); 
            
            assertFalse(mano.getEsperandoRespuesta());
            assertTrue(mano.getJugadorTurno() ==1);
            assertEquals(4, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderNoQuieroValecuatro() throws Exception{
        setup(0, 4);
        setupTruco(0, 3); //Se canto Retruco
        setupCartasDisponibles(1, 2);
        setupSecuenciaCantos(1, 1, 0, 1, null, null);
        try {
            manoService.cantosTruco(CantosTruco.VALECUATRO,codigo);
            assertTrue(mano.getJugadorTurno() ==2);


            manoService.responderTruco(RespuestasTruco.NO_QUIERO,codigo);
            assertEquals(3, mano.getPuntosTruco());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Test
    public void responderTrucoRetrucoQuiero() throws Exception{
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
        try {
            manoService.cantosTruco(CantosTruco.TRUCO,codigo);
            assertTrue(mano.getJugadorTurno() == 1);

            manoService.responderTruco(RespuestasTruco.SUBIR,codigo); //RETRUCO
            assertEquals(2, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo); 
            assertEquals(3, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Test
    public void responderRetrucoValeCuatroQuiero() throws Exception{
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(3, 2);
        setupSecuenciaCantos(0, 1, null, null, null, null);
        
        try {
            manoService.cantosTruco(CantosTruco.RETRUCO,codigo);
            assertTrue(mano.getJugadorTurno() == 0);

            manoService.responderTruco(RespuestasTruco.SUBIR,codigo); //VALECUATRO
            assertEquals(3, mano.getPuntosTruco());
            assertEquals(3, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo); 
            assertEquals(4, mano.getPuntosTruco());
            assertEquals(3, mano.getJugadorTurno());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Test //OBVIAMENTE ESTE TEST HAY QUE SEPARARLO TODO, PERO QUE FUNCIONE ME VA A HACER LLORAR DE LA EMOCION
    public void responderTrucoRetrucoValecuatroQuiero() throws Exception{
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
        try {
            manoService.cantosTruco(CantosTruco.TRUCO,codigo); //TRUCO
            assertEquals(1,mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.SUBIR,codigo);//RETRUCO
            assertEquals(2, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.SUBIR,codigo);//VALECUATRO
            assertEquals(3, mano.getPuntosTruco());
            assertEquals(1, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO,codigo); //QUIERO
            assertEquals(4, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
    


    @Test
    public void testRepartirCartas() {
       setup(0,4);
            
        CartaRepository cartaRepository = Mockito.mock(CartaRepository.class);
        Carta carta = mock(Carta.class);

        when(cartaRepository.findById(anyInt())).thenReturn(Optional.of(carta));
        
        manoService = new ManoService(mano, cartaRepository);
    
        List<List<Carta>> cartasRepartidas = manoService.repartirCartas(partida);
    
            
        assertNotNull(cartasRepartidas);
        assertEquals(4, cartasRepartidas.size()); // 4 jugadores
        cartasRepartidas.forEach(cartas -> assertEquals(3, cartas.size())); // Cada jugador recibe 3 cartas
    
    }



}
