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
        // 1) Creamos la partida y la mano
        partida = new Partida();
        partida.setNumJugadores(2);
        partida.setJugadorMano(0);
        partida.setCodigo("PARTIDA_TEST");
        partida.setConFlor(true);
        mano = new Mano();
        mano.setPartida(partida);

        // 2) Inicializamos sus valores
        mano.setJugadorTurno(partida.getJugadorMano());
        mano.setRondaActual(1);       // Por defecto, empieza en primera ronda
        mano.setPuntosTruco(1);      // Truco en “estado base”
        mano.setPuntosEnvido(0);     // Sin envido
        mano.setFloresCantadas(0);   // Nadie cantó Flor aún
        mano.setGanadoresRondas(new ArrayList<>(List.of(0, 0)));

        // 3) Inicializamos la estructura para cartas
        //    (aquí, 2 jugadores => 2 “sublistas”)
        List<List<Carta>> cartasBase = new ArrayList<>();
        cartasBase.add(new ArrayList<>()); // Jugador 0
        cartasBase.add(new ArrayList<>()); // Jugador 1
        mano.setCartasDisp(cartasBase);

        // 4) Cartas lanzadas en la ronda
        List<Carta> lanzadasRonda = new ArrayList<>();
        lanzadasRonda.add(null);
        lanzadasRonda.add(null);
        mano.setCartasLanzadasRonda(lanzadasRonda);
    }

    // ----------------------------
    // MÉTODOS AUXILIARES
    // ----------------------------

    private void asignarCartasTresMismoPalo(Integer jugador) {
        // Jugador con 3 cartas del mismo palo
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
    }

    private void asignarCartasDistintoPalo(Integer jugador) {
        // Jugador con 3 cartas de diferentes palos
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
    }

    // ----------------------------
    // TESTS: comprobarSiPuedeCantarFlor()
    // ----------------------------

    @Test
    public void testComprobarSiPuedeCantarFlor_Exitoso() {
        // Jugador 0 tiene 3 cartas del mismo palo
        asignarCartasTresMismoPalo(0);
        mano.setJugadorTurno(0);

        // Cumplimos las condiciones:
        mano.setPuntosTruco(1);  // Sin subir
        mano.setPuntosEnvido(0); // Sin envido
        mano.setRondaActual(1);  // Primera ronda
        mano.setFloresCantadas(0);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertTrue(puede);
        assertTrue(mano.getPuedeCantarFlor());
        assertEquals(1, mano.getQueFlorPuedeCantar()); // Se setea en 1 => FLOR
        assertTrue(mano.getTresMismoPalo());           // Se detecta 3 cartas mismo palo
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_SinTresCartasMismoPalo() {
        // Jugador 0 NO tiene 3 cartas del mismo palo
        asignarCartasDistintoPalo(0);
        mano.setJugadorTurno(0);

        // Resto de condiciones ok
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        // queFlorPuedeCantar se queda en 0 => Nada
        assertEquals(0, mano.getQueFlorPuedeCantar());
        // tresMismoPalo en false => no se detectó
        assertFalse(mano.getTresMismoPalo());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_SinRonda1() {
        // Jugador 0 SÍ tiene 3 cartas del mismo palo
        asignarCartasTresMismoPalo(0);
        mano.setJugadorTurno(0);

        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        // Ronda actual = 2 => no cumple la condición
        mano.setRondaActual(2);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_TrucoDiferenteDe1() {
        // Jugador 0 SÍ tiene 3 cartas del mismo palo
        asignarCartasTresMismoPalo(0);
        mano.setJugadorTurno(0);

        mano.setPuntosTruco(2); // Se subió el Truco
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
        assertFalse(mano.getTresMismoPalo()); 
        // No llegó ni a setTresMismoPalo(true) porque la condición no se cumplió.
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_FloresCantadasYaEsMayor() {
        // Jugador 0 SÍ tiene 3 cartas del mismo palo
        asignarCartasTresMismoPalo(0);
        mano.setJugadorTurno(0);

        // Condiciones
        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);
        // Pero ya hay 2 flores cantadas => no se puede subir más
        mano.setFloresCantadas(2);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertFalse(puede);
        assertFalse(mano.getPuedeCantarFlor());
        // Se setea queFlorPuedeCantar en 0 => nada
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testComprobarSiPuedeCantarFlor_YaCantaronUnaFlor() {
        // Jugador 0 con 3 cartas
        asignarCartasTresMismoPalo(0);
        mano.setJugadorTurno(0);

        mano.setPuntosTruco(1);
        mano.setPuntosEnvido(0);
        mano.setRondaActual(1);
        // Se ha cantado Flor previamente => 1
        mano.setFloresCantadas(1);

        Boolean puede = mano.comprobarSiPuedeCantarFlor();
        assertTrue(puede);
        assertTrue(mano.getPuedeCantarFlor());
        // queFlorPuedeCantar = 2 => Contraflor / Con flor me achico
        assertEquals(2, mano.getQueFlorPuedeCantar());
    }

    // ----------------------------
    // TESTS: crearListaTantosCadaJugadorFlor()
    // ----------------------------

    @Test
    public void testcrearListaTantosCadaJugadorFlor_TodosSinFlor() {
        // 2 jugadores => ninguno con 3 cartas mismo palo
        asignarCartasDistintoPalo(0);  // Jugador 0 => sin flor
        asignarCartasDistintoPalo(1);  // Jugador 1 => sin flor

        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();
        // Nadie tiene flor => se ponen en 0
        assertEquals(2, lista.size());
        assertEquals(0, lista.get(0));
        assertEquals(null, lista.get(1));
    }

    @Test
    public void testcrearListaTantosCadaJugadorFlor_UnoTieneFlor() {
        // Jugador 0 => 3 cartas mismo palo
        asignarCartasTresMismoPalo(0);
        // Jugador 1 => distinto palo
        asignarCartasDistintoPalo(1);

        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();
        // Jugador 0 => > 0, Jugador 1 => 0
        assertEquals(2, lista.size());
        assertTrue(lista.get(0) > 0);
        assertEquals(null, lista.get(1)); //Si no tiene flor, es null

        // Comprueba también equipoGanadorFlor
        assertEquals(0, mano.getEquipoGanadorFlor()); // Gana el equipo 0
    }

    @Test
    public void testcrearListaTantosCadaJugadorFlor_AmbosTienenFlor() {
        // Jugador 0 => 3 copas
        asignarCartasTresMismoPalo(0);
        // Jugador 1 => 3 espadas
        List<Carta> jug1 = new ArrayList<>();
        Carta e1 = new Carta(); e1.setId(10); e1.setPalo(Palo.ESPADAS); e1.setValor(1);
        Carta e2 = new Carta(); e2.setId(11); e2.setPalo(Palo.ESPADAS); e2.setValor(2);
        Carta e3 = new Carta(); e3.setId(12); e3.setPalo(Palo.ESPADAS); e3.setValor(3);
        mano.getCartasDisp().get(1).add(e1);
        mano.getCartasDisp().get(1).add(e2);
        mano.getCartasDisp().get(1).add(e3);

        mano.setJugadorTurno(0);

        List<Integer> lista = mano.crearListaTantosCadaJugadorFlor();
        // Ambos > 0 => Empate => Gana "Mano" => equipo 0
        assertTrue(lista.get(0) > 0);
        assertTrue(lista.get(1) ==null);
        // Normalmente, si hay empate de flor y el mano es eq0 => eq0 mantiene
        // la flor, y anula la del eq1
        // Según tu lógica, al empatar, “nuevaLista.set(i, null)” para uno de los bandos
        // El “mano” conserva su valor => eq0
        assertNull(lista.get(1));
        assertEquals(0, mano.getEquipoGanadorFlor());
    }
}
