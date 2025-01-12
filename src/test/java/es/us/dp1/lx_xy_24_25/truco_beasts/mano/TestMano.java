package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;


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
        List<List<Carta>> cartas =  List.of(jugador1, jugador2, jugador3, jugador4);
        mano.setCartasDisp(cartas);
        return cartas;
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

    public void setUpTantos(Integer jug0, Integer jug1, Integer jug2, Integer jug3) {
        mano.setCartasDisp(List.of(
            generaCartasParaTanto(jug0),
            generaCartasParaTanto(jug1),
            generaCartasParaTanto(jug2),
            generaCartasParaTanto(jug3)
        ));
    }
    
    private List<Carta> generaCartasParaTanto(Integer tanto) {
    
    
        if (tanto >= 20) {
            Integer valor = tanto - 20;
            Integer valor1 = valor / 2;
            Integer valor2 = valor - valor1;
            return List.of(
                setUpCarta(Palo.ESPADAS, valor1 ==0?11:valor1),
                setUpCarta(Palo.ESPADAS, valor2==0?10:valor2),
                setUpCarta(Palo.BASTOS, 1) 
            );
        }
        if(tanto< 20 && tanto > 7){
            return List.of(setUpCarta(Palo.NADA
                , tanto));
        }
    
    
       
        
        return List.of(
            setUpCarta(Palo.OROS, tanto),
            setUpCarta(Palo.COPAS, 1),
            setUpCarta(Palo.BASTOS, 1) 
        );
    }
    
    
   
    
    //Crear lista de tantos envido
    @Test
    public void envidosGanaEquipo2(){
        setup(0, 4);
        setUpTantos(7, 21,4, 6);
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(7);
        listaResultante.add(21);
        listaResultante.add(null);
        listaResultante.add(null);
        
        
        mano.setJugadorTurno(0);
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido());
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
        
    }

    @Test
    public void envidosElManoTieneLaMasAlta(){
        setup(0, 4);
        setUpTantos(33, 2, 2, 21);
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(33);
        listaResultante.add(null);
        listaResultante.add(null);
        listaResultante.add(null);
        
        
        mano.setJugadorTurno(0);
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(0, mano.getEquipoGanadorEnvido());
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
        
    }

    @Test
    public void envidosEmpateGanaElEquipoMano(){
        setup(1, 4);
        setUpTantos(7, 24, 24, 6);
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(null);
        listaResultante.add(24);
        listaResultante.add(null);
        listaResultante.add(null);
        
        
        mano.setJugadorTurno(1);
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido());
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
        
    }

    @Test
    public void envidosGanaElEquipoNoMano(){
        setup(3, 4);
        setUpTantos(7, 25, 24, 6);
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(7);
        listaResultante.add(25);
        listaResultante.add(null);
        listaResultante.add(6);
        
        
        mano.setJugadorTurno(3);
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido());
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
        
    }

    @Test
    public void envidosTodosDicenSuTanto(){
        setup(0, 4);
        setUpTantos(7, 20, 24, 33);
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(7);
        listaResultante.add(20);
        listaResultante.add(24);
        listaResultante.add(33);
        
        
        mano.setJugadorTurno(0);
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido());
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
        
    }
    @Test
    public void envidosEquipoManoMasBajo() {
        setup(2, 4);
        setUpTantos(1, 2, 3, 4);  
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(null);
        listaResultante.add(null);
        listaResultante.add(3);
        listaResultante.add(4);
  
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido()); 
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
    }
    @Test
    public void envidosEquipoContrarioMasAlto() {
        setup(1, 4);
        setUpTantos(5, 21, 26, 33);  // Puntajes diferentes SIEMPRE MENORES QUE 7 o ENTRE 20 y 33.
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(null);
        listaResultante.add(21);
        listaResultante.add(26);
        listaResultante.add(33);
        
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(1, mano.getEquipoGanadorEnvido());  // El equipo contrario (equipo 1) gana.
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
    }
    @Test
    public void envidosEmpateGanaEquipoMano() {
        setup(0, 4);
        setUpTantos(30, 30, 20, 20);  
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(30);
        listaResultante.add(null);
        listaResultante.add(null);
        listaResultante.add(null);
        

        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(0, mano.getEquipoGanadorEnvido());  
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
    }
    @Test
    public void envidosEquipoManoMasAlto() {
        setup(1, 4);
        setUpTantos(25, 20, 21, 22);  
        
        List<Integer> listaResultante = new ArrayList<>();
        listaResultante.add(25);
        listaResultante.add(20);
        listaResultante.add(21);
        listaResultante.add(22);
        
        mano.crearListaTantosCadaJugador();
        List<Integer> listaTantosCadaJugadorRegistrados = mano.getEnvidosCadaJugador();
        assertEquals(0, mano.getEquipoGanadorEnvido());  
        assertEquals(listaResultante, listaTantosCadaJugadorRegistrados);
    }
            

    
    
    //Agrupa cartas
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

   
    // get max puntuacion
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
    @Test
    public void maxPuntuacionTresCartasMismoPaloValoresBajos() {
        Map<Palo, List<Carta>> cartasMismoPalo = setUpCartasPaloValorTresIguales(); 
        assertEquals(32, mano.getMaxPuntuacion(cartasMismoPalo)); 
    }
    @Test
    public void maxPuntuacionTresCartasDiferentes() {
        Map<Palo, List<Carta>> cartasDiferentesPalos = setUpCartasPaloValorTresDiferentes();
        assertEquals(3, mano.getMaxPuntuacion(cartasDiferentesPalos)); 
    }

    @Test
    public void maxPuntuacionDosCartasMismoPalo() {
        List<Carta> cartasJugador = List.of(
            setUpCarta(Palo.ESPADAS, 7),
            setUpCarta(Palo.ESPADAS, 5),
            setUpCarta(Palo.COPAS, 3)
        );
        Map<Palo, List<Carta>> agrupacion = mano.agrupaCartasPalo(cartasJugador);
        assertEquals(32, mano.getMaxPuntuacion(agrupacion)); // 20 + 7 + 5.
    }
    @Test
    public void maxPuntuacionUnaCarta() {
        List<Carta> cartasJugador = List.of(setUpCarta(Palo.ESPADAS, 6));
        Map<Palo, List<Carta>> agrupacion = mano.agrupaCartasPalo(cartasJugador);
        assertEquals(6, mano.getMaxPuntuacion(agrupacion)); 
    }


    

    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTS");
        mano.setJugadorTurno(jugMano);
        mano.setPartida(partida);
        List<Integer> ganadoresRonda = new ArrayList<>();
        
        ganadoresRonda.add(0);
        ganadoresRonda.add(0);
        mano.setGanadoresRondas(ganadoresRonda);
        mano.setFloresCantadas(0);
        ganadoresRonda.add(0);
        mano.setEnvidosCantados(ganadoresRonda);
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
        Integer actual = mano.getJugadorTurno();
        mano.setJugadorTurno(mano.siguienteJugador(actual));
        assertEquals(1, mano.getJugadorTurno());
         actual = mano.getJugadorTurno();

         mano.setJugadorTurno(mano.siguienteJugador(actual));
         assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteDe4() {
        setup(0,4);
        mano.setJugadorTurno(2);
        Integer actual = mano.getJugadorTurno();
        mano.setJugadorTurno(mano.siguienteJugador(actual));
        assertEquals(3, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAdelanteUltimo() {
        setup(0,4);

        mano.setJugadorTurno(3);
        Integer actual = mano.getJugadorTurno();
        mano.setJugadorTurno(mano.siguienteJugador(actual));
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe2() {
        setup(0,2);
        mano.setJugadorTurno(0);
        Integer actual = mano.getJugadorTurno();

        mano.setJugadorTurno(mano.obtenerJugadorAnterior(actual));

        assertEquals(1, mano.getJugadorTurno());
        actual = mano.getJugadorTurno();

        mano.setJugadorTurno(mano.obtenerJugadorAnterior(actual));        
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasDe4() {
        setup(0,4);
        mano.setJugadorTurno(3);
        Integer actual = mano.getJugadorTurno();

        mano.setJugadorTurno(mano.obtenerJugadorAnterior(actual));
        assertEquals(2, mano.getJugadorTurno());
    }

    @Test
    public void turnoHaciaAtrasUltimo() {
        setup(0,4);

        mano.setJugadorTurno(0);
        Integer actual = mano.getJugadorTurno();

        mano.setJugadorTurno(mano.obtenerJugadorAnterior(actual));        
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
    //COMPROBAR SI PUEDE CANTAR ENVIDO
    @Test
    public void sePuedeCantarEnvidoDeDos(){
        setup(0, 1);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        assertTrue(mano.comprobarSiPuedeCantarEnvido(true));
        Integer actual = mano.getJugadorTurno();
        mano.setJugadorTurno(mano.siguienteJugador(actual));
        assertTrue(mano.comprobarSiPuedeCantarEnvido(true));
    }
    @Test
    public void sePuedeCantarEnvidoNoEsPie(){
        setup(2, 4);
        mano.setJugadorTurno(3);
        setUpCartas(setUpListaCartasDispo(3));
        assertFalse(mano.comprobarSiPuedeCantarEnvido(true));
    }

    @Test
    public void sePuedeCantarEnvidoSiEsPie(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(1);
        assertTrue(mano.comprobarSiPuedeCantarEnvido(true));
    }
    @Test
    public void sePuedeCantarEnvidoSiEsPieOtroEquipo(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        assertTrue(mano.comprobarSiPuedeCantarEnvido(true));
    }
    @Test
    public void sePuedeCantarEnvidoHayTruco(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        mano.setPuntosTruco(2);
        assertFalse(mano.comprobarSiPuedeCantarEnvido(true));
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaDos(){
        setup(2, 4);
        setUpCartas(setUpListaCartasDispo(2));
        mano.setJugadorTurno(0);
        mano.setRondaActual(2);
        assertFalse(mano.comprobarSiPuedeCantarEnvido(true)); 
    }
    @Test
    public void sePuedeCantarEnvidoYaEsRondaTres(){
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(1));
        mano.setJugadorTurno(3);
        mano.setRondaActual(2);
        assertFalse(mano.comprobarSiPuedeCantarEnvido(true));
    }

    @Test
    public void sePuedeCantarEnvidoYaSeCanto(){
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(3);
        mano.setPuntosEnvido(2);
        assertFalse(mano.comprobarSiPuedeCantarEnvido(true));
    }
    @Test
    public void noSePuedeCantarFaltaEnvidoPorMaximo() {
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        mano.setEnvidosCantados(Arrays.asList(1, 1, mano.getMaximosFaltaEnvido())); // falta envido maximo alcanzado
        assertFalse(mano.comprobarSiPuedeCantarEnvido(false));
    }

    @Test
    public void soloSePuedeCantarRealEnvidoPorMaximo() {
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        mano.setEnvidosCantados(Arrays.asList(1, mano.getMaximosRealEnvido(), 0)); // real envido maximo alcanzado
        assertTrue(mano.comprobarSiPuedeCantarEnvido(false));
        assertEquals(1, mano.getQueEnvidoPuedeCantar()); 
    }

    @Test
    public void soloSePuedeCantarEnvidoPorMaximo() {
        setup(0, 4);
        setUpCartas(setUpListaCartasDispo(3));
        mano.setJugadorTurno(0);
        mano.setEnvidosCantados(Arrays.asList(mano.getMaximosEnvido(), 0, 0)); // envido maximo alcanzado
        assertTrue(mano.comprobarSiPuedeCantarEnvido(false));
        assertEquals(2, mano.getQueEnvidoPuedeCantar()); 
    }

    
    //Cercano a mano
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
 
    @Test
    public void cercanoAManoUnSoloJugadorEsMano() {
        setup(0, 4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); 
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(0, preferido);
    }
    @Test
    public void cercanoAManoUnSoloJugadorNoEsMano() {
        setup(0, 4);
    
        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(2); 
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(2, preferido);
    }
    @Test
    public void cercanoAManoTodosMismoEquipo() {
        setup(0, 4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); jugadores.add(2); jugadores.add(4);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(0, preferido); 
    }
    @Test
    public void cercanoAManoTodosDistintoEquipo() {
        setup(0, 4);
    
        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(1); jugadores.add(3);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(1, preferido); 
    }
    @Test
    public void cercanoAManoMixtoEquipos() {
        setup(0, 4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(1); jugadores.add(2); jugadores.add(3);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(2, preferido); 
    }
    @Test
    public void cercanoAManoManoNoCero() {
        setup(3, 4);
    
        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); jugadores.add(2); jugadores.add(3);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(3, preferido);
    }
    @Test
    public void cercanoAManoCircular() {
        setup(3, 4);

        List<Integer> jugadores = new ArrayList<>();
        jugadores.add(0); jugadores.add(1);
        Integer preferido = mano.cercanoAMano(jugadores);
        assertEquals(1, preferido); 
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

    //Comparar cartas
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

    @Test
    public void compararCartasTodasIguales() {
        setup(0, 4);
        setupCartasLanzadas(10, 10, 10, 10);

        Integer empezador = mano.compararCartas();
        assertEquals(0, empezador); // Gana la mano por ser el jugador más cercano.
    }

    @Test
    public void compararCartasOrdenDescendente() {
        setup(0, 4);
        setupCartasLanzadas(14, 10, 6, 2);
    
        Integer empezador = mano.compararCartas();
        assertEquals(0, empezador); 
    }
    @Test
    public void compararCartasEmpateUltimosJugadores() {
        setup(0, 4);
        setupCartasLanzadas(6, 7, 14, 14);

        Integer empezador = mano.compararCartas();
        assertEquals(2, empezador); // El jugador 2 gana por ser mano
    }
    @Test
    public void compararCartasEmpateConsecutivos() {
        setup(0, 4);
        setupCartasLanzadas(6, 10, 10, 4);
    
        Integer empezador = mano.compararCartas();
        assertEquals(2, empezador); // Gana el jugador 2 por mano.
    }
    
    @Test
    public void compararCartasEmpateMultipleCercania() {
        setup(1, 4);
        setupCartasLanzadas(10, 10, 10, 10);
    
        Integer empezador = mano.compararCartas();
        assertEquals(1, empezador); // Gana el jugador 1 por ser el mano.
    }
    


    //
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


    //COMPROBAR SI PUEDE CANTAR TRUCO
    @Test 
    public void puedeCantarTrucoNoSeCanto(){
        setup(0, 4);
        setupTruco(null, null);
        mano.setJugadorTurno(3);
        assertTrue(mano.comprobarSiPuedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(0, 2);
        mano.setJugadorTurno(3);
        assertTrue(mano.comprobarSiPuedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipo(){
        setup(0, 4);
        setupTruco(1, 2);
        mano.setJugadorTurno(3);
        assertFalse(mano.comprobarSiPuedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(1);
        assertTrue(mano.comprobarSiPuedeCantarTruco());
    }

    @Test 
    public void puedeCantarTrucoNoLoCantoElOtroEquipoDe2(){
        setup(0, 2);
        setupTruco(0, 2);
        mano.setJugadorTurno(0);
        assertFalse(mano.comprobarSiPuedeCantarTruco());
    }
    @Test
    public void noPuedeCantarTrucoPorPuntosMaximos() {
        setup(0, 4); 
        setupTruco(null, null); 
        mano.setPuntosTruco(mano.getPuntosMaximosDelTruco()); 
        mano.setJugadorTurno(1);
        assertFalse(mano.comprobarSiPuedeCantarTruco());
    }

   // 3 cartas mismo palo
   @Test
    public void tresCartasDelMismoPalo() {
        List<Carta> cartasMismoPalo = setUpListaCartasDispo(3).get(2); 
        assertTrue(mano.tiene3CartasMismoPalo(cartasMismoPalo));
    }
    @Test
    public void tresCartasDeDiferentesPalos() {
        List<Carta> cartasDiferentesPalos = setUpListaCartasDispo(3).get(1); 
        assertFalse(mano.tiene3CartasMismoPalo(cartasDiferentesPalos));
    }
    @Test
    public void unaSolaCarta() {
        List<Carta> unaCarta = List.of(setUpListaCartasDispo(1).get(0).get(0)); 
        assertFalse(mano.tiene3CartasMismoPalo(unaCarta));
    }
    @Test
    public void listaVacia() {
        List<Carta> listaVacia = new ArrayList<>();
        assertFalse(mano.tiene3CartasMismoPalo(listaVacia)); 
    }

    //Gestionar ganadores ronda:
    @Test
    public void gestionarGanadoresRondaGanaEquipo1() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Collections.emptyList(), 0); 
    
        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(1, 0), ganadoresRonda); 
    }
    @Test
    public void gestionarGanadoresRondaGanaEquipo2() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Collections.emptyList(), 1); 

        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(0, 1), ganadoresRonda); 
    }
    @Test
    public void gestionarGanadoresRondaEmpateAmbosEquipos() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Arrays.asList(0, 1), null); 
    
        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(1, 1), ganadoresRonda); 
    }
    @Test
    public void gestionarGanadoresRondaEmpateEquipo1() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0));
        mano.gestionarGanadoresRonda(Arrays.asList(0, 2), null); 

        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(1, 0), ganadoresRonda); 
    }
    @Test
    public void gestionarGanadoresRondaEmpateEquipo2() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Arrays.asList(1, 3), null); 

        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(0, 1), ganadoresRonda); 
    }

    @Test
    public void gestionarGanadoresRondaSinJugadores() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Collections.emptyList(), null); 
    
        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(0, 1), ganadoresRonda); // por defecto como hay un else se le suma al equipo 2, se podria agregar otra condicion 
                                                                //pero no tiene mucho sentido 7a que en el juego no puede "no ganar" ningun equipo
    }
    @Test
    public void gestionarGanadoresRondaEmpateYGanador() {
        setup(0, 4);
        mano.setGanadoresRondas(Arrays.asList(0, 0)); 
        mano.gestionarGanadoresRonda(Arrays.asList(0, 1), 0); 

        List<Integer> ganadoresRonda = mano.getGanadoresRondas();
        assertEquals(Arrays.asList(1, 0), ganadoresRonda); 
    }

    //Quien responde y quien responde flor:
    @Test
    public void quienRespondeJugadorActualEsIniciador() {
        setup(0, 4); 
        mano.setJugadorIniciadorDelCanto(1);
        mano.setJugadorTurno(1); 

        Integer jugadorQueResponde = mano.quienResponde();
        assertEquals(2, jugadorQueResponde); 
    }
    @Test
    public void quienRespondeJugadorActualNoEsIniciador() {
        setup(0, 4); 
        mano.setJugadorIniciadorDelCanto(1);
        mano.setJugadorTurno(2); // El jugador actual no es el iniciador.

        Integer jugadorQueResponde = mano.quienResponde();
        assertEquals(1, jugadorQueResponde); // El iniciador debe responder.
    }
    @Test
    public void quienRespondeCircularidad() {
        setup(0, 4); 
        mano.setJugadorIniciadorDelCanto(3); // Iniciador es el último jugador.
        mano.setJugadorTurno(3); // El jugador actual es el iniciador.
    
        Integer jugadorQueResponde = mano.quienResponde();
        assertEquals(0, jugadorQueResponde); // El siguiente jugador (el 0) responde.
    }
    @Test
    public void quienRespondeFlorJugadorActualNoEsIniciador() {
        setup(0, 4);
        mano.setJugadorIniciadorDelCanto(1);
        mano.setJugadorTurno(2); // Jugador actual no es el iniciador.
        mano.setListaTienenFlores(List.of(true, true, false, false)); 

        Integer jugadorQueResponde = mano.quienRespondeFlor();
        assertEquals(1, jugadorQueResponde); // El iniciador debe responder.
    }
    @Test
    public void quienRespondeFlorEquipoContrarioTieneFlor() {
        setup(0, 4);
        mano.setJugadorIniciadorDelCanto(1);
        mano.setJugadorTurno(1); 
        mano.setListaTienenFlores(List.of(false, true, true, false)); 
    
        Integer jugadorQueResponde = mano.quienRespondeFlor();
        assertEquals(2, jugadorQueResponde); 
    }
    @Test
    public void quienRespondeFlorCircularidad() {
        setup(3, 6); 
        mano.setJugadorIniciadorDelCanto(3);
        mano.setJugadorTurno(3); 
        mano.setListaTienenFlores(List.of(false, true, true, true, false, false)); //Tiene flor el otro el que esta dando TOODA la vuelta
                                                            //resp,    canta
        Integer jugadorQueResponde = mano.quienRespondeFlor();
        assertEquals(2, jugadorQueResponde); 
    }
    
    @Test
    public void quienRespondeFlorNadieTieneFlor() {
        setup(0, 4);
        mano.setJugadorIniciadorDelCanto(1);
        mano.setJugadorTurno(2);
        mano.setListaTienenFlores(List.of(false, false, false, false)); // Nadie tiene flor.
    
        Integer jugadorQueResponde = mano.quienRespondeFlor();
        assertEquals(1, jugadorQueResponde); 
    }
    @Test
    public void quienRespondeFlorTodosTienenFlor() {
        setup(0, 4);
        mano.setJugadorIniciadorDelCanto(0);
        mano.setJugadorTurno(0); 
        mano.setListaTienenFlores(List.of(true, true, true, true)); 
    
        Integer jugadorQueResponde = mano.quienRespondeFlor();
        assertEquals(1, jugadorQueResponde); 
    }
                

    //TESTS de copia parcial del truco
    @Test
    public void copiaParcialTrucoTest() {
        Mano manoOrigen = new Mano();
        manoOrigen.setEquipoCantor(1);
        manoOrigen.setEsperandoRespuesta(true);
        manoOrigen.setJugadorTurno(2);
        manoOrigen.setPuntosTruco(15);
        manoOrigen.setQueEnvidoPuedeCantar(2);
        manoOrigen.setPuedeCantarEnvido(true);
        manoOrigen.setTerminada(false);
        manoOrigen.setUltimoMensaje(Cantos.TRUCO);

        Mano manoDestino = new Mano();

        manoDestino.copiaParcialTruco(manoOrigen);

        assertEquals(1, manoDestino.getEquipoCantor());
        assertTrue(manoDestino.getEsperandoRespuesta());
        assertEquals(2, manoDestino.getJugadorTurno());
        assertEquals(15, manoDestino.getPuntosTruco());
        assertEquals(2, manoDestino.getQueEnvidoPuedeCantar());
        assertTrue(manoDestino.getPuedeCantarEnvido());
        assertFalse(manoDestino.getTerminada());
        assertEquals(Cantos.TRUCO, manoDestino.getUltimoMensaje());
    }
    @Test
    public void copiaParcialTrucoConValoresPorDefecto() {
        Mano manoOrigen = new Mano();
        
        manoOrigen.setEquipoCantor(0);
        manoOrigen.setEsperandoRespuesta(false);
        manoOrigen.setJugadorTurno(0);
        manoOrigen.setPuntosTruco(0);
        manoOrigen.setQueEnvidoPuedeCantar(0);
        manoOrigen.setPuedeCantarEnvido(false);
        manoOrigen.setTerminada(false);
        manoOrigen.setUltimoMensaje(null);

        Mano manoDestino = new Mano();

        manoDestino.copiaParcialTruco(manoOrigen);

        assertEquals(0, manoDestino.getEquipoCantor());
        assertFalse(manoDestino.getEsperandoRespuesta());
        assertEquals(0, manoDestino.getJugadorTurno());
        assertEquals(0, manoDestino.getPuntosTruco());
        assertEquals(0, manoDestino.getQueEnvidoPuedeCantar());
        assertFalse(manoDestino.getPuedeCantarEnvido());
        assertFalse(manoDestino.getTerminada());
        assertEquals(null, manoDestino.getUltimoMensaje());
    }
    @Test
    public void copiaParcialTrucoSinRefenciasCompartidas() {
        Mano manoOrigen = new Mano();
        manoOrigen.setEquipoCantor(1);
        manoOrigen.setEsperandoRespuesta(true);
        manoOrigen.setJugadorTurno(2);
        manoOrigen.setPuntosTruco(15);
        manoOrigen.setQueEnvidoPuedeCantar(2);
        manoOrigen.setPuedeCantarEnvido(true);
        manoOrigen.setTerminada(false);
        manoOrigen.setUltimoMensaje(Cantos.TRUCO);

        Mano manoDestino = new Mano();
        
        manoDestino.copiaParcialTruco(manoOrigen);

        manoOrigen.setEquipoCantor(99);
        manoOrigen.setUltimoMensaje(Cantos.CONTRAFLOR);

        // Verificar que los valores en la mano destino no se modificaron
        assertNotEquals(99, manoDestino.getEquipoCantor());
        assertNotEquals(Cantos.CONTRAFLOR, manoDestino.getUltimoMensaje());
        assertEquals(1, manoDestino.getEquipoCantor());  // El valor original
        assertEquals(Cantos.TRUCO, manoDestino.getUltimoMensaje());  // El valor original
    }
    @Test
    public void copiaParcialTrucoConValoresNulos() {
        Mano manoOrigen = new Mano();
        manoOrigen.setEquipoCantor(1);
        manoOrigen.setEsperandoRespuesta(null);  
        manoOrigen.setJugadorTurno(null);  
        manoOrigen.setPuntosTruco(null);  
        manoOrigen.setQueEnvidoPuedeCantar(null);  
        manoOrigen.setPuedeCantarEnvido(null);  
        manoOrigen.setTerminada(null);  
        manoOrigen.setUltimoMensaje(null);  

        Mano manoDestino = new Mano();

        manoDestino.copiaParcialTruco(manoOrigen);

        assertEquals(1, manoDestino.getEquipoCantor());
        assertNull(manoDestino.getEsperandoRespuesta());
        assertNull(manoDestino.getJugadorTurno());
        assertNull(manoDestino.getPuntosTruco());
        assertNull(manoDestino.getQueEnvidoPuedeCantar());
        assertNull(manoDestino.getPuedeCantarEnvido());
        assertNull(manoDestino.getTerminada());
        assertNull(manoDestino.getUltimoMensaje());
    }
    @Test
    public void copiaParcialTrucoConValoresLimite() {
        Mano manoOrigen = new Mano();
        manoOrigen.setEquipoCantor(Integer.MAX_VALUE);  // Valor máximo
        manoOrigen.setEsperandoRespuesta(true);
        manoOrigen.setJugadorTurno(Integer.MIN_VALUE);  // Valor mínimo
        manoOrigen.setPuntosTruco(Integer.MAX_VALUE);  // Valor máximo
        manoOrigen.setQueEnvidoPuedeCantar(0);
        manoOrigen.setPuedeCantarEnvido(true);
        manoOrigen.setTerminada(false);
        manoOrigen.setUltimoMensaje(Cantos.TRUCO);
    
        Mano manoDestino = new Mano();
    
        manoDestino.copiaParcialTruco(manoOrigen);
    
        assertEquals(Integer.MAX_VALUE, manoDestino.getEquipoCantor());
        assertTrue(manoDestino.getEsperandoRespuesta());
        assertEquals(Integer.MIN_VALUE, manoDestino.getJugadorTurno());
        assertEquals(Integer.MAX_VALUE, manoDestino.getPuntosTruco());
        assertEquals(0, manoDestino.getQueEnvidoPuedeCantar());
        assertTrue(manoDestino.getPuedeCantarEnvido());
        assertFalse(manoDestino.getTerminada());
        assertEquals(Cantos.TRUCO, manoDestino.getUltimoMensaje());
    }
    @Test
    public void copiaParcialTrucoConValoresPreestablecidos() {
        Mano manoOrigen = new Mano();
        manoOrigen.setEquipoCantor(1);
        manoOrigen.setEsperandoRespuesta(true);
        manoOrigen.setJugadorTurno(2);
        manoOrigen.setPuntosTruco(15);
        manoOrigen.setQueEnvidoPuedeCantar(2);
        manoOrigen.setPuedeCantarEnvido(true);
        manoOrigen.setTerminada(false);
        manoOrigen.setUltimoMensaje(Cantos.TRUCO);
    
        Mano manoDestino = new Mano();
        manoDestino.setEquipoCantor(99);  
        manoDestino.setEsperandoRespuesta(false);  
        manoDestino.setJugadorTurno(99);  
        manoDestino.setPuntosTruco(99);  
        manoDestino.setQueEnvidoPuedeCantar(99);  
        manoDestino.setPuedeCantarEnvido(false);  
        manoDestino.setTerminada(true);  
        manoDestino.setUltimoMensaje(Cantos.QUIERO); 
    
        manoDestino.copiaParcialTruco(manoOrigen);
    
        // Verificar que los valores de la mano destino se hayan sobrescrito correctamente
        assertEquals(1, manoDestino.getEquipoCantor());
        assertTrue(manoDestino.getEsperandoRespuesta());
        assertEquals(2, manoDestino.getJugadorTurno());
        assertEquals(15, manoDestino.getPuntosTruco());
        assertEquals(2, manoDestino.getQueEnvidoPuedeCantar());
        assertTrue(manoDestino.getPuedeCantarEnvido());
        assertFalse(manoDestino.getTerminada());
        assertEquals(Cantos.TRUCO, manoDestino.getUltimoMensaje());
    }
    
        

}
