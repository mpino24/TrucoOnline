package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

public class TestManoService {

    Partida partida = new Partida();
    Mano mano = new Mano();
    ManoService manoService = null;
    
    public void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        mano.setPartida(partida);
        manoService = new ManoService(mano);
    }

    @Test
    public void devuelveSiguienteJugadorDe2() {
        setup(0,4);

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

        Integer siguienteJugador = manoService.siguienteJugador(5);
        assertEquals(0, siguienteJugador);
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

    public void setupCartasDisponibles(Integer jugadorActual, Integer ronda) {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
        c0.setPoder(14);
        c1.setPoder(13);
        c2.setPoder(6);
        mano.setJugadorTurno(jugadorActual);
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

    






}
