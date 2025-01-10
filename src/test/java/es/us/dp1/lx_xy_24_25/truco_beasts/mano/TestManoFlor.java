package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;

public class TestManoFlor {

    private Mano mano;
    private Partida partida;

    @BeforeEach
    public void setup() {
        partida = new Partida();
        partida.setNumJugadores(2);
        partida.setJugadorMano(0);
        partida.setCodigo("PARTIDA_TEST");
        partida.setConFlor(true);

        mano = new Mano();
        mano.setPartida(partida);
        mano.setJugadorTurno(partida.getJugadorMano());
        mano.setRondaActual(1);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setFloresCantadas(0);
        mano.setGanadoresRondas(new ArrayList<>(List.of(0, 0)));
        List<List<Carta>> cartasBase = new ArrayList<>();
        cartasBase.add(new ArrayList<>());
        cartasBase.add(new ArrayList<>());
        mano.setCartasDisp(cartasBase);

        List<Carta> lanzadasRonda = new ArrayList<>();
        lanzadasRonda.add(null);
        lanzadasRonda.add(null);
        mano.setCartasLanzadasRonda(lanzadasRonda);
    }

    private void asignarCartasTresMismoPalo(Integer jugador) {
        Carta c1 = new Carta();
        c1.setId(1);
        c1.setPalo(Palo.COPAS);
        c1.setValor(4);

        Carta c2 = new Carta();
        c2.setId(2);
        c2.setPalo(Palo.COPAS);
        c2.setValor(5);

        Carta c3 = new Carta();
        c3.setId(3);
        c3.setPalo(Palo.COPAS);
        c3.setValor(6);

        mano.getCartasDisp().get(jugador).add(c1);
        mano.getCartasDisp().get(jugador).add(c2);
        mano.getCartasDisp().get(jugador).add(c3);

        mano.setCartasNoBorradas(mano.getCartasDisp());
    }

    private void asignarCartasDistintoPalo(Integer jugador) {
        Carta c1 = new Carta();
        c1.setId(10);
        c1.setPalo(Palo.ESPADAS);
        c1.setValor(4);

        Carta c2 = new Carta();
        c2.setId(20);
        c2.setPalo(Palo.OROS);
        c2.setValor(5);

        Carta c3 = new Carta();
        c3.setId(30);
        c3.setPalo(Palo.COPAS);
        c3.setValor(6);

        mano.getCartasDisp().get(jugador).add(c1);
        mano.getCartasDisp().get(jugador).add(c2);
        mano.getCartasDisp().get(jugador).add(c3);

        mano.setCartasNoBorradas(mano.getCartasDisp());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_Exitoso() {
        asignarCartasTresMismoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);
        mano.setFloresCantadas(0);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertTrue(puede);
        assertTrue(mano.getPuedeCantarFlor());
        assertEquals(1, mano.getQueFlorPuedeCantar());
        assertTrue(mano.getTresMismoPalo());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_SinTresCartasMismoPalo() {
        asignarCartasDistintoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
        assertFalse(mano.getTresMismoPalo());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_SinRonda1() {
        asignarCartasTresMismoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(2);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_TrucoDiferenteDe1() {
        asignarCartasTresMismoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(2);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
        assertFalse(mano.getTresMismoPalo());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_FloresCantadasYaEsMayor() {
        asignarCartasTresMismoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);
        mano.setFloresCantadas(2);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_YaCantaronUnaFlor() {
        asignarCartasTresMismoPalo(0);

        mano.setJugadorTurno(0);
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);
        mano.setFloresCantadas(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();

        assertTrue(puede);
        assertTrue(mano.getPuedeCantarFlor());
        assertEquals(2, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testcrearListaTantosCadaJugadorFlor_TodosSinFlor() {
        asignarCartasDistintoPalo(0);
        asignarCartasDistintoPalo(1);

        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();

        assertEquals(2, lista.size());
        assertEquals(0, lista.get(0));
        assertEquals(null, lista.get(1));
    }

    @Test
    public void testcrearListaTantosCadaJugadorFlor_UnoTieneFlor() {
        asignarCartasTresMismoPalo(0);
        asignarCartasDistintoPalo(1);

        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();

        assertEquals(2, lista.size());
        assertTrue(lista.get(0) > 0);
        assertEquals(null, lista.get(1));
        assertEquals(0, mano.getEquipoGanadorFlor());
    }

    @Test
    public void testcrearListaTantosCadaJugadorFlor_AmbosTienenFlor() {
        asignarCartasTresMismoPalo(0);

        Carta e1 = new Carta(); e1.setId(10); e1.setPalo(Palo.ESPADAS); e1.setValor(1);
        Carta e2 = new Carta(); e2.setId(11); e2.setPalo(Palo.ESPADAS); e2.setValor(2);
        Carta e3 = new Carta(); e3.setId(12); e3.setPalo(Palo.ESPADAS); e3.setValor(3);

        mano.getCartasDisp().get(1).add(e1);
        mano.getCartasDisp().get(1).add(e2);
        mano.getCartasDisp().get(1).add(e3);
        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();
        
        assertTrue(lista.get(0) > 0);
        assertTrue(lista.get(1) == null);
        assertNull(lista.get(1));
        assertEquals(0, mano.getEquipoGanadorFlor());
    }
}
