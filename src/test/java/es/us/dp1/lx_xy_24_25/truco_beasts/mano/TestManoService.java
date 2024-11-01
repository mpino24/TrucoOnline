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
    }

    @Test
    public void devuelveSiguienteJugadorUltimo() {
        setup(0,4);

        Integer siguienteJugador = manoService.siguienteJugador(5);
        assertEquals(0, siguienteJugador);
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

    public void setupCartasDisponibles(Integer jugadorActual) {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
        c0.setPoder(14);
        c1.setPoder(13);
        c2.setPoder(6);
        mano.setJugadorTurno(jugadorActual);
        List<Carta> listaBase = List.of(c0,c1,c2);
        mano.setCartasDisp(List.of(listaBase, listaBase, listaBase, listaBase));
        mano.setCartasLanzadasRonda(List.of(null, null, null, null));
    }

    @Test
    public void tirarCartaMano() {
        setup(0,4);
        setupCartasDisponibles(0);

        manoService.tirarCarta(0);
        Integer tam = mano.getCartasDisp().get(0).size();
        assertEquals(2, tam);
    }
}
