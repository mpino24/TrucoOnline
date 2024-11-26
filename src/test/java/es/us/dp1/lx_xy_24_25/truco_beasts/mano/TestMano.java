package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.CantosTruco;
import es.us.dp1.lx_xy_24_25.truco_beasts.patronEstadoTruco.RespuestasTruco;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;

public class TestMano {
    Carta carta = new Carta();
    Mano mano = new Mano();
    Partida partida = new Partida();

   
    @ParameterizedTest
    @ValueSource(ints= {10,11,12})
    public void asignarValorCartaNumero10(Integer value) {
        Integer valorResultante = mano.comprobarValor(value);
        assertEquals(0, valorResultante);
    }
    @ParameterizedTest
    @ValueSource(ints= {1,2,4,6,7})
    public void asignarValorCartaNumeroMayor10(Integer valor) {
        Integer valorResultante = mano.comprobarValor(valor);
        assertEquals(valor, valorResultante);
    }

    public Carta setUpCarta(Palo p, Integer valor){
        Carta carta= new Carta();
        carta.setPalo(p);
        carta.setValor(valor);
        return carta;
    }

    public List<List<Carta>> setUpListaCartasDispo(Integer cartasPorJugador){
        Carta carta1= setUpCarta(Palo.ESPADAS, 10);
        Carta carta2= setUpCarta(Palo.ESPADAS, 5);
        Carta carta3= setUpCarta(Palo.OROS, 6);

        Carta carta4= setUpCarta(Palo.BASTOS, 3);
        Carta carta5= setUpCarta(Palo.ESPADAS, 2);
        Carta carta6= setUpCarta(Palo.COPAS, 3);

        Carta carta7= setUpCarta(Palo.OROS, 5);
        Carta carta8= setUpCarta(Palo.OROS, 7);
        Carta carta9= setUpCarta(Palo.OROS, 12);

        Carta carta10= setUpCarta(Palo.COPAS, 11);
        Carta carta11= setUpCarta(Palo.BASTOS, 6);
        Carta carta12= setUpCarta(Palo.ESPADAS, 4);
        
        List<Carta> jugador1;
        List<Carta> jugador2;
        List<Carta> jugador3;
        List<Carta> jugador4;

        switch (cartasPorJugador) {
            case 1:
                jugador1= List.of(carta1);
                jugador2= List.of(carta4);
                jugador3= List.of(carta7);
                jugador4= List.of(carta10);
                break;
            case 2:
                jugador1= List.of(carta1, carta2);
                jugador2= List.of(carta4, carta5);
                jugador3= List.of(carta7, carta8);
                jugador4= List.of(carta10, carta11);
                break;
            default:
                jugador1= List.of(carta1, carta2, carta3);
                jugador2= List.of(carta4, carta5, carta6);
                jugador3= List.of(carta7, carta8, carta9);
                jugador4= List.of(carta10, carta11, carta12);
                break;
        }

        return List.of(jugador1, jugador2, jugador3, jugador4);

    }


    public Map<Palo, List<Carta>> setUpCartasPaloValorTresIguales(){
        List<Carta> cartasJug3=setUpListaCartasDispo(3).get(2);
        Map<Palo, List<Carta>> listaPaloValoresJug=mano.agrupaCartasPalo(cartasJug3);
        return listaPaloValoresJug;
    }

    public Map<Palo, List<Carta>> setUpCartasPaloValorTresDiferentes(){
        List<Carta> cartasJug2=setUpListaCartasDispo(3).get(1);
        Map<Palo, List<Carta>> listaPaloValoresJug=mano.agrupaCartasPalo(cartasJug2);
        return listaPaloValoresJug;
    }
   

    public void setUpCartas(List<List<Carta>> cartasDisponibles){
        mano.setCartasDisp(cartasDisponibles);
    }

    @Test
    public void envidosListaLlena(){
        
        List<List<Carta>> listaCartasDispo = setUpListaCartasDispo(3);
        setUpCartas(listaCartasDispo);
        List<Integer> listaResultante = List.of(25,3,32,6);
        List<Integer> listaEnvidosRegistrados = mano.listaEnvidos(mano.getCartasDisp());
        assertEquals(listaResultante, listaEnvidosRegistrados);
        
    }

    @Test
    public void envidosListaVacia(){
        setUpCartas(new ArrayList<>());
        List<Integer> listaEnvidosRegistrados = mano.listaEnvidos(mano.getCartasDisp());
        assertEquals(new ArrayList<>(), listaEnvidosRegistrados);
    }

    

    @Test
    public void agrupaCartasPorPalo(){

        List<Carta> cartasDeJugador1= setUpListaCartasDispo(3).get(0);

        List<Carta> cartasDeEspada= Arrays.asList(setUpListaCartasDispo(3).get(0).get(0), setUpListaCartasDispo(3).get(0).get(1));
        List<Carta> cartasDeOro=  Arrays.asList(setUpListaCartasDispo(3).get(0).get(2)); 

        Map<Palo, List<Carta>> paloCartas= new HashMap<>();
        paloCartas.put(Palo.ESPADAS, cartasDeEspada);
        paloCartas.put(Palo.OROS, cartasDeOro);
        
        assertEquals(paloCartas, mano.agrupaCartasPalo(cartasDeJugador1));

    }
   
    @Test
    public void puntuacionDeJugadorCorrecta3Iguales(){
        Map<Palo, List<Carta>> listaPaloValoresJug3= setUpCartasPaloValorTresIguales();
        assertEquals(32, mano.getMaxPuntuacion(listaPaloValoresJug3));
    }


    @Test
    public void puntuacionDeJugadorCorrecta3Diferentes(){
        Map<Palo, List<Carta>> listaPaloValoresJug2= setUpCartasPaloValorTresDiferentes();
        assertEquals(3, mano.getMaxPuntuacion(listaPaloValoresJug2));
    }

    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTS");
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);
    }
 
    @Test
    public void devuelveSiguienteJugadorDe2() {
        setup(0,2);

        Integer siguienteJugador = mano.siguienteJugador(0);
        assertEquals(1, siguienteJugador);
        siguienteJugador = mano.siguienteJugador(1);
        assertEquals(0, siguienteJugador);
    }

    @Test
    public void devuelveSiguienteJugadorDe4() {
        setup(0,4);
        Integer siguienteJugador = mano.siguienteJugador(0);
        assertEquals(1, siguienteJugador);
    }

    @Test
    public void devuelveSiguienteJugadorUltimo() {
        setup(0,4);

        Integer siguienteJugador = mano.siguienteJugador(3);
        assertEquals(0, siguienteJugador);
    }

    @Test
    public void turnoHaciaAdelanteDe2() {
        setup(0,2);
        mano.setJugadorTurno(0);
        mano.siguienteTurno();
        assertEquals(1, mano.getJugadorTurno());
        mano.siguienteTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteDe4() {
        setup(0,4);
        mano.setJugadorTurno(2);
        mano.siguienteTurno();
        assertEquals(3, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteUltimo() {
        setup(0,4);

        mano.setJugadorTurno(3);
        mano.siguienteTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe2() {
        setup(0,2);
        mano.setJugadorTurno(0);
        mano.anteriorTurno();
        assertEquals(1, mano.getJugadorTurno());
        mano.anteriorTurno();
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe4() {
        setup(0,4);
        mano.setJugadorTurno(3);
        mano.anteriorTurno();
        assertEquals(2, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasUltimo() {
        setup(0,4);

        mano.setJugadorTurno(0);
        mano.anteriorTurno();
        assertEquals(3, mano.getJugadorTurno());
    }

    @Test
    public void devuelveJugadorPieDe2(){
        setup(0, 2);
        Integer pie = mano.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(1, pie);
    }
    @Test
    public void devuelveJugadorPieDe4(){
        setup(0, 4);
        Integer pie = mano.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(3, pie);
    }

    @Test
    public void devuelveJugadorPieDe4OtroMano(){
        setup(2, 4);
        Integer pie = mano.obtenerJugadorAnterior(mano.getPartida().getJugadorMano());
        assertEquals(1, pie);
        //El pie del otro equipo
        Integer otroPie = mano.obtenerJugadorAnterior(pie);
        assertEquals(0, otroPie);
    }
    @Test 
    public void devuelveJugadorPieMetodoEspecifico(){
        setup(3, 6);
        Integer pie = mano.obtenerJugadorPie();
        assertEquals(2, pie);
    }

    @Test
    public void obtenerRondaActualRondaUno(){
        setup(3, 4);
        setUpCartas(setUpListaCartasDispo(3));
        Integer ronda = mano.obtenerRondaActual();
        assertEquals(1, ronda);
    }
    @Test
    public void obtenerRondaActualRondaDos(){
        setup(3, 4);
        setUpCartas(setUpListaCartasDispo(2));
        Integer ronda = mano.obtenerRondaActual();
        assertEquals(2, ronda);
    }
    @Test
    public void obtenerRondaActualRondaTres(){
        setup(3, 4);
        setUpCartas(setUpListaCartasDispo(1));
        Integer ronda = mano.obtenerRondaActual();
        assertEquals(3, ronda);
    }


    @Test
    public void sePuedeCantarEnvidoDeDos(){
        setup(0, 1);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        assertTrue(mano.puedeCantarEnvido());
        mano.siguienteTurno();
        assertTrue(mano.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoNoEsPie(){
        setup(2, 4);
        mano.setJugadorTurno(3);
        setUpCartas(setUpListaCartasDispo(3));
        assertFalse(mano.puedeCantarEnvido());
    }

    @Test
    public void sePuedeCantarEnvidoSiEsPie(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(1);
        assertTrue(mano.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoSiEsPieOtroEquipo(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        assertTrue(mano.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoHayTruco(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        mano.setPuntosTruco(2);
        assertFalse(mano.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaDos(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(2));
        mano.setJugadorTurno(0);
        assertFalse(mano.puedeCantarEnvido());
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaTres(){
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(1));
        mano.setJugadorTurno(3);
        assertFalse(mano.puedeCantarEnvido());
    }

    @Test
    public void sePuedeCantarEnvidoYaSeCanto(){
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(3);
        mano.setPuntosEnvido(2);
        assertFalse(mano.puedeCantarEnvido());
    }
    
    @Test
    public void cercanoAManoConMano() {
        setup(0,4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); jugadores.add(1);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(0, preferido);
    }

    @Test
    public void cercanoAManoSinManoDistintoEquipo() {
        setup(0,4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(1); jugadores.add(2);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(2, preferido);
    }

    @Test
    public void cercanoAManoSinManoMismoEquipo() {
        setup(0,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(2); jugadores.add(4);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(2, preferido);
    }

    @Test
    public void cercanoAManoSinManoMismoEquipoNoMano() {
        setup(0,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(3); jugadores.add(5);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(3, preferido);
    }

    @Test
    public void cercanoAManoManoNoCeroMismoEquipoNoMano() {
        setup(4,6);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(3); jugadores.add(5);
        Integer preferido = mano.cercanoAMano(jugadores);
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

        Integer empezador = mano.compararCartas();
        assertEquals(2, empezador);
    }

    @Test
    public void compararCartasEmpateYCartaAlta() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 6, 14);

        Integer empezador = mano.compararCartas();
        assertEquals(3, empezador);
    }

    @Test
    public void compararCartasEmpateDistintoEquipo() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 6, 4);

        Integer empezador = mano.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateTresPersonas() {
        setup(0,4);
        setupCartasLanzadas(10, 10, 10, 4);

        Integer empezador = mano.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateMismoEquipo() {
        setup(0,4);
        setupCartasLanzadas(10, 6, 10, 4);

        Integer empezador = mano.compararCartas();
        assertEquals(0, empezador);
    }

    @Test
    public void compararCartasEmpateMismoEquipoSinMano() {
        setup(0,4);
        setupCartasLanzadas(4, 10, 6, 10);

        Integer empezador = mano.compararCartas();
        assertEquals(1, empezador);
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

        for(int i = 0;i <numJugadores; i++){
            cartasRonda.add(null);
            cartasDisponibles.add(listaBase);
        }
        mano.setCartasDisp(cartasDisponibles);
        mano.setCartasLanzadasRonda(cartasRonda);
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
    public void cantaTruco() {
        setup(0, 4);
        setupTruco(null, null);
        setupCartasDisponibles(0, 1);
        
        mano.cantosTruco(CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
       
    }

    

    @Test 
    public void cantaRetruco() {
        setup(0, 4);
        setupTruco(1, 2);
        setupCartasDisponibles(0, 2);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        
        
        mano.cantosTruco(CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
        
    }

    @Test 
    public void cantaValecuatro() {
        setup(0, 4);
        setupTruco(1, 3);
        setupCartasDisponibles(0, 2);
        setupSecuenciaCantos(0,1, 1, 1, null, null);
        
        mano.cantosTruco(CantosTruco.VALECUATRO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
        
    }

    @Test
    public void responderQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        
        mano.cantosTruco(CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        mano.responderTruco(RespuestasTruco.QUIERO);
        assertTrue(mano.getJugadorTurno() ==0);
        assertEquals(2, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null); //No se canto
        setupCartasDisponibles(0, 1);
        
        
        mano.cantosTruco(CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        mano.responderTruco(RespuestasTruco.NO_QUIERO);
        assertEquals(1, mano.getPuntosTruco());
        
    }

    @Test
    public void responderQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(1, 1);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        
       
        mano.cantosTruco(CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2);

        mano.responderTruco(RespuestasTruco.QUIERO); //QUIERO
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(3, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(1, 1);
        setupSecuenciaCantos(1, 1, null, null, null, null);
        
        mano.cantosTruco(CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2); 

        mano.responderTruco(RespuestasTruco.NO_QUIERO); //NO QUIERO
        assertEquals(2, mano.getPuntosTruco());
        
    }

    @Test
    public void responderQuieroValecuatro() {
        setup(0, 4);
        setupTruco(0, 3); //Se canto Retruco
        setupCartasDisponibles(1, 2);
        setupSecuenciaCantos(1, 1, 0, 1, null, null);
        
        
        mano.cantosTruco(CantosTruco.VALECUATRO);
        assertEquals(2, mano.getJugadorTurno());
        assertTrue(mano.getEsperandoRespuesta());

        mano.responderTruco(RespuestasTruco.QUIERO); 
            
        assertFalse(mano.getEsperandoRespuesta());
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(4, mano.getPuntosTruco());
        
    }

    @Test
    public void responderNoQuieroValecuatro(){
        setup(0, 4);
        setupTruco(0, 3); //Se canto Retruco
        setupCartasDisponibles(1, 2);
        setupSecuenciaCantos(1, 1, 0, 1, null, null);
        
        mano.cantosTruco(CantosTruco.VALECUATRO);
        assertTrue(mano.getJugadorTurno() ==2);


        mano.responderTruco(RespuestasTruco.NO_QUIERO);
        assertEquals(3, mano.getPuntosTruco());
        
    }


    @Test
    public void responderTrucoRetrucoQuiero() {
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
       
        mano.cantosTruco(CantosTruco.TRUCO);
        assertTrue(mano.getJugadorTurno() == 1);

        mano.responderTruco(RespuestasTruco.SUBIR); //RETRUCO
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        mano.responderTruco(RespuestasTruco.QUIERO); 
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
        
    }

    @Test
    public void responderRetrucoValeCuatroQuiero() {
        setup(0, 4);
        setupTruco(0, 2); //Se canto Truco
        setupCartasDisponibles(3, 2);
        setupSecuenciaCantos(0, 1, null, null, null, null);
        
        
        mano.cantosTruco(CantosTruco.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 0);

        mano.responderTruco(RespuestasTruco.SUBIR); //VALECUATRO
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());

        mano.responderTruco(RespuestasTruco.QUIERO); 
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());
        
    }


    @Test 
    public void responderTrucoRetrucoValecuatroQuiero() {
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
        
        mano.cantosTruco(CantosTruco.TRUCO); //TRUCO
        assertEquals(1,mano.getJugadorTurno());

        mano.responderTruco(RespuestasTruco.SUBIR);//RETRUCO
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        mano.responderTruco(RespuestasTruco.SUBIR);//VALECUATRO
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(1, mano.getJugadorTurno());

        mano.responderTruco(RespuestasTruco.QUIERO); //QUIERO
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
        
    }

    @Test 
    public void cantarTrucoNoPuede() {
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);

        mano.cantosTruco(CantosTruco.TRUCO); //TRUCO
        mano.responderTruco(RespuestasTruco.QUIERO); //QUIERO
        
        TrucoException exception = assertThrows(TrucoException.class, 
        () -> mano.cantosTruco(CantosTruco.TRUCO));
    
        
        assertEquals("No podes cantar truco ni ninguna de sus variantes", exception.getMessage());
    }
    @Test 
    public void cantarTrucoYaSeCanto() {
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);

        mano.cantosTruco(CantosTruco.TRUCO); //TRUCO
        mano.responderTruco(RespuestasTruco.QUIERO); //QUIERO
        mano.siguienteTurno();
        assertTrue(mano.puedeCantarTruco()); //Puede cantar el retruco

        TrucoException exception = assertThrows(TrucoException.class, 
        () -> mano.cantosTruco(CantosTruco.TRUCO));
    
        
        assertEquals("Ya se canto el truco", exception.getMessage());
        
    }
    @Test 
    public void cantarRetrucoNoSeCantoTruco() {
        setup(0, 4);
        setupTruco(null, null); //No se canto nada
        setupCartasDisponibles(0, 1);

    
        TrucoException exception = assertThrows(TrucoException.class, 
        () -> mano.cantosTruco(CantosTruco.RETRUCO));
    
        
        assertEquals("No se cantó el truco", exception.getMessage());
        
    }
    
}
