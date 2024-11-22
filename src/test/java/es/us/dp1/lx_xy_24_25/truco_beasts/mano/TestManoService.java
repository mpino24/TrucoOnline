package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;



public class TestManoService {



    Partida partida = new Partida();
    Mano mano = new Mano();

    
    ManoService manoService = null;

   
    CartaRepository cartaRepository;


    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);
        
        manoService = new ManoService(mano, cartaRepository);
         
    }

    @Test
    public void devuelveSiguienteJugadorDe2() {
        setup(0,2);

        Integer siguienteJugador = manoService.siguienteJugador(0);
        assertEquals(1, siguienteJugador);
        siguienteJugador = manoService.siguienteJugador(1);
        assertEquals(0, siguienteJugador);
    }

    @Test
    public void devuelveSiguienteJugadorDe4() {
        setup(0,4);
        Integer siguienteJugador = manoService.siguienteJugador(0);
        assertEquals(1, siguienteJugador);
    }

    @Test
    public void devuelveSiguienteJugadorUltimo() {
        setup(0,4);

        Integer siguienteJugador = manoService.siguienteJugador(3);
        assertEquals(0, siguienteJugador);
    }

    @Test
    public void turnoHaciaAdelanteDe2() {
        setup(0,2);
        mano.setJugadorTurno(0);
        manoService.siguienteTurno();
        assertEquals(1, mano.getJugadorTurno());
        manoService.siguienteTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteDe4() {
        setup(0,4);
        mano.setJugadorTurno(2);
        manoService.siguienteTurno();
        assertEquals(3, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteUltimo() {
        setup(0,4);

        mano.setJugadorTurno(3);
        manoService.siguienteTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe2() {
        setup(0,2);
        mano.setJugadorTurno(0);
        manoService.anteriorTurno();
        assertEquals(1, mano.getJugadorTurno());
        manoService.anteriorTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe4() {
        setup(0,4);
        mano.setJugadorTurno(3);
        manoService.anteriorTurno();
        assertEquals(2, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasUltimo() {
        setup(0,4);

        mano.setJugadorTurno(0);
        manoService.anteriorTurno();
        assertEquals(3, mano.getJugadorTurno());
    }
    

    @Test
    public void devuelveJugadorPieDe2(){
        setup(0, 2);
        Integer pie = manoService.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(1, pie);
    }
    @Test
    public void devuelveJugadorPieDe4(){
        setup(0, 4);
        Integer pie = manoService.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(3, pie);
    }

    @Test
    public void devuelveJugadorPieDe4OtroMano(){
        setup(2, 4);
        Integer pie = manoService.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(1, pie);
        //El pie del otro equipo
        Integer otroPie = manoService.obtenerJugadorAnterior(pie);
        assertEquals(0, otroPie);
    }
    @Test 
    public void devuelveJugadorPieMetodoEspecifico(){
        setup(3, 6);
        Integer pie = manoService.obtenerJugadorPie();
        assertEquals(2, pie);
    }

    @Test
    public void obtenerRondaActualRondaUno(){
        setup(3, 4);
        setupCartasDisponibles(2, 1);
        Integer ronda = manoService.obtenerRondaActual();
        assertEquals(1, ronda);
    }
    @Test
    public void obtenerRondaActualRondaDos(){
        setup(3, 4);
        setupCartasDisponibles(2, 2);
        Integer ronda = manoService.obtenerRondaActual();
        assertEquals(2, ronda);
    }
    @Test
    public void obtenerRondaActualRondaTres(){
        setup(3, 4);
        setupCartasDisponibles(2, 3);
        Integer ronda = manoService.obtenerRondaActual();
        assertEquals(3, ronda);
    }


    @Test
    public void sePuedeCantarEnvidoDeDos(){
        setup(0, 1);
        setupCartasDisponibles(0,1);
        assertTrue(manoService.puedeCantarEnvido());
        manoService.siguienteTurno();
        assertTrue( manoService.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoNoEsPie(){
        setup(2, 4);
        setupCartasDisponibles(3,1);
        assertFalse(manoService.puedeCantarEnvido());
    }

    @Test
    public void sePuedeCantarEnvidoSiEsPie(){
        setup(2, 4);
        setupCartasDisponibles(1,1);
        assertTrue( manoService.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoSiEsPieOtroEquipo(){
        setup(2, 4);
        setupCartasDisponibles(0,1);
        assertTrue(manoService.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoHayTruco(){
        setup(2, 4);
        setupCartasDisponibles(0,1);
        mano.setPuntosTruco(2);
        assertFalse( manoService.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaDos(){
        setup(2, 4);
        setupCartasDisponibles(0,2);
        assertFalse( manoService.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaTres(){
        setup(0, 4);
        setupCartasDisponibles(3,3);
        assertFalse( manoService.puedeCantarEnvido());
    }

    @Test
    public void sePuedeCantarEnvidoYaSeCanto(){
        setup(0, 4);
        setupCartasDisponibles(3,1);
        mano.setPuntosEnvido(2);
        assertFalse( manoService.puedeCantarEnvido());
    }
    
    @Test
    public void cercanoAManoConMano() {
        setup(0,4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); jugadores.add(1);
        Integer preferido = manoService.cercanoAMano(jugadores);
        assertEquals(0, preferido);
    }

    @Test
    public void cercanoAManoSinManoDistintoEquipo() {
        setup(0,4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(1); jugadores.add(2);
        Integer preferido = manoService.cercanoAMano(jugadores);
        assertEquals(2, preferido);
    }

    @Test
    public void cercanoAManoSinManoMismoEquipo() {
        setup(0,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(2); jugadores.add(4);
        Integer preferido = manoService.cercanoAMano(jugadores);
        assertEquals(2, preferido);
    }

    @Test
    public void cercanoAManoSinManoMismoEquipoNoMano() {
        setup(0,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(3); jugadores.add(5);
        Integer preferido = manoService.cercanoAMano(jugadores);
        assertEquals(3, preferido);
    }

    @Test
    public void cercanoAManoManoNoCeroMismoEquipoNoMano() {
        setup(4,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(3); jugadores.add(5);
        Integer preferido = manoService.cercanoAMano(jugadores);
        assertEquals(5, preferido);
    }

    public void setupCartasLanzadas(Integer poder0, Integer poder1, Integer poder2, Integer poder3) {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
        Carta c3 = new Carta();
        c0.setPoder(poder0);
        c1.setPoder(poder1);
        c2.setPoder(poder2);
        c3.setPoder(poder3);
        mano.setCartasLanzadasRonda(List.of(c0, c1, c2, c3));
    }

    @Test
    public void compararCartasCartaAlta() {
        setup(0,4);
        setupCartasLanzadas(1, 2, 14, 6);

        Integer empezador = manoService.compararCartas();
        assertEquals(2, empezador);
    }

    @Test
    public void compararCartasEmpateYCartaAlta() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 6, 14);

        Integer empezador = manoService.compararCartas();
        assertEquals(3, empezador);
    }

    @Test
    public void compararCartasEmpateDistintoEquipo() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 6, 4);

        Integer empezador = manoService.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateTresPersonas() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 10, 4);

        Integer empezador = manoService.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateMismoEquipo() {
        setup(0,4);
        setupCartasLanzadas(10, 6, 10, 4);

        Integer empezador = manoService.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateMismoEquipoSinMano() {
        setup(0,4);
        setupCartasLanzadas(4, 10, 6, 10);

        Integer empezador = manoService.compararCartas();
        assertEquals(1, empezador);
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

        manoService.tirarCarta(cartaALanzar);
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

        manoService.tirarCarta(cartaALanzar);
        
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
        
    }
    @Test
    public void tirarCartaSeAvanzaDeTurno() {
        Integer jugadorActual = 0;
        Integer cartaALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(cartaALanzar);
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
        assertTrue(manoService.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(0, 2);
        mano.setJugadorTurno(3);
        assertTrue(manoService.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(1, 2);
        mano.setJugadorTurno(3);
        assertFalse(manoService.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(1);
        assertTrue(manoService.puedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(0);
        assertFalse(manoService.puedeCantarTruco());
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
            manoService.cantosTruco(CantosTruco.TRUCO);
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
            manoService.cantosTruco(CantosTruco.RETRUCO);
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
            manoService.cantosTruco(CantosTruco.VALECUATRO);
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
            manoService.cantosTruco(CantosTruco.TRUCO);
            assertTrue(mano.getJugadorTurno() ==1);

            manoService.responderTruco(RespuestasTruco.QUIERO);
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
            manoService.cantosTruco(CantosTruco.TRUCO);
            assertTrue(mano.getJugadorTurno() ==1);

            manoService.responderTruco(RespuestasTruco.NO_QUIERO);
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
            manoService.cantosTruco(CantosTruco.RETRUCO);
            assertTrue(mano.getJugadorTurno() ==2);

            manoService.responderTruco(RespuestasTruco.QUIERO); //QUIERO
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
            manoService.cantosTruco(CantosTruco.RETRUCO);
            assertTrue(mano.getJugadorTurno() ==2); 

            manoService.responderTruco(RespuestasTruco.NO_QUIERO); //NO QUIERO
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
            manoService.cantosTruco(CantosTruco.VALECUATRO);
            assertEquals(2, mano.getJugadorTurno());
            assertTrue(mano.getEsperandoRespuesta());

            manoService.responderTruco(RespuestasTruco.QUIERO); //QUIERO
            // HAY QUE SEPARAR LOS TESTS, PERO AHORA ME DA PAJA
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
            manoService.cantosTruco(CantosTruco.VALECUATRO);
            assertTrue(mano.getJugadorTurno() ==2);


            manoService.responderTruco(RespuestasTruco.NO_QUIERO);
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
            manoService.cantosTruco(CantosTruco.TRUCO);
            assertTrue(mano.getJugadorTurno() == 1);

            manoService.responderTruco(RespuestasTruco.SUBIR); //RETRUCO
            assertEquals(2, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO); 
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
            manoService.cantosTruco(CantosTruco.RETRUCO);
            assertTrue(mano.getJugadorTurno() == 0);

            manoService.responderTruco(RespuestasTruco.SUBIR); //VALECUATRO
            assertEquals(3, mano.getPuntosTruco());
            assertEquals(3, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO); 
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
            manoService.cantosTruco(CantosTruco.TRUCO); //TRUCO
            assertEquals(1,mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.SUBIR);//RETRUCO
            assertEquals(2, mano.getPuntosTruco());
            assertEquals(0, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.SUBIR);//VALECUATRO
            assertEquals(3, mano.getPuntosTruco());
            assertEquals(1, mano.getJugadorTurno());

            manoService.responderTruco(RespuestasTruco.QUIERO); //QUIERO
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
