package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.EnvidoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;

public class TestManoServiceFlor {

    Partida partida;
    Mano mano;
    String codigo;
    ManoService manoService;

    // Mocks
    CartaRepository cartaRepository;
    PartidaService partidaService;
    PartidaJugadorService partidaJugadorService;
    
    /**
     * Se ejecuta antes de cada test. 
     * Creamos la Partida, la Mano y el ManoService "mokeado".
     */
    @BeforeEach
    public void setupBase() {
        partida = new Partida();
        mano = new Mano();

        partida.setNumJugadores(2);    // Por defecto, 2 jugadores
        partida.setJugadorMano(0);     // El jugador mano es el 0
        partida.setCodigo("TESTFLOR"); // Código de ejemplo

        mano.setPartida(partida);
        mano.setJugadorTurno(partida.getJugadorMano());
        mano.setGanadoresRondas(new ArrayList<>(List.of(0, 0)));

        // Inicializamos la lista envidosCantados con 3 ceros
        List<Integer> envidos = new ArrayList<>(List.of(0, 0, 0));
        mano.setEnvidosCantados(envidos);

        // Inicializamos los mocks
        cartaRepository = mock(CartaRepository.class);
        partidaService = mock(PartidaService.class);
        partidaJugadorService = mock(PartidaJugadorService.class);

        // Configuramos la partida en el mock
        when(partidaService.findPartidaByCodigo(partida.getCodigo()))
            .thenReturn(partida);

        manoService = new ManoService(cartaRepository, partidaService, partidaJugadorService);

        // Actualizamos la mano en el map interno de manoService
        codigo = partida.getCodigo();
        manoService.actualizarMano(mano, codigo);
    }

    // ------------------------------------------------
    // MÉTODOS AUXILIARES PARA ASIGNAR CARTAS
    // ------------------------------------------------

    /**
     * Asigna 3 cartas del mismo palo al 'jugador' indicado,
     * de modo que tenga Flor.
     */
    private void setupCartasDisponiblesConFlor(Integer jugador) {
        Carta c1 = new Carta(); 
        c1.setId(1); 
        c1.setPalo(Palo.ESPADAS); 
        c1.setPoder(7); 
        c1.setValor(7);

        Carta c2 = new Carta(); 
        c2.setId(2); 
        c2.setPalo(Palo.ESPADAS); 
        c2.setPoder(6); 
        c2.setValor(6);

        Carta c3 = new Carta(); 
        c3.setId(3); 
        c3.setPalo(Palo.ESPADAS); 
        c3.setPoder(5);
        c3.setValor(5);

        List<List<Carta>> cartasTotales = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            cartasTotales.add(new ArrayList<>());
        }
        // Solo añadimos las 3 cartas al 'jugador' con Flor
        cartasTotales.get(jugador).add(c1);
        cartasTotales.get(jugador).add(c2);
        cartasTotales.get(jugador).add(c3);

        mano.setCartasDisp(cartasTotales);

        // Ronda = 1
        mano.setRondaActual(1);

        // Cartas lanzadas Ronda (nulos)
        List<Carta> lanzadas = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            lanzadas.add(null);
        }
        mano.setCartasLanzadasRonda(lanzadas);

        mano.setJugadorTurno(jugador);
    }

    /**
     * Asigna 3 cartas de palos distintos para que NO tenga Flor.
     */
    private void setupCartasDisponiblesSinFlor(Integer jugador) {
        Carta c1 = new Carta(); 
        c1.setId(1); 
        c1.setPalo(Palo.ESPADAS);
        c1.setPoder(7); 
        c1.setValor(7);

        Carta c2 = new Carta(); 
        c2.setId(2); 
        c2.setPalo(Palo.OROS);
        c2.setPoder(6); 
        c2.setValor(6);

        Carta c3 = new Carta(); 
        c3.setId(3); 
        c3.setPalo(Palo.COPAS);
        c3.setPoder(5);
        c3.setValor(5);

        List<List<Carta>> cartasTotales = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            cartasTotales.add(new ArrayList<>());
        }
        cartasTotales.get(jugador).add(c1);
        cartasTotales.get(jugador).add(c2);
        cartasTotales.get(jugador).add(c3);

        mano.setCartasDisp(cartasTotales);

        mano.setRondaActual(1);

        List<Carta> lanzadas = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            lanzadas.add(null);
        }
        mano.setCartasLanzadasRonda(lanzadas);

        mano.setJugadorTurno(jugador);
    }

    // ------------------------------------------------
    // TESTS RELACIONADOS CON CANTOS DE FLOR
    // ------------------------------------------------

    @Test
    public void testCantarFlorExitoso() {
        // Jugador 0 tiene Flor
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(0);
        mano.setEsperandoRespuesta(false);

        // Canta Flor
        manoService.cantosFlor(codigo, Cantos.FLOR);

        // Debe haberse incrementado las floresCantadas
        assertEquals(1, mano.getFloresCantadas());

        // El turno pasa al que responde
        // JugadorIniciador = 0 => quienResponde se calcula => 
        // en 2 jugadores, quienResponde = 1
        assertEquals(1, mano.getJugadorTurno());

        // Debe estar esperando respuesta
        assertTrue(mano.getEsperandoRespuesta());

        // Último mensaje
        assertEquals(Cantos.FLOR, mano.getUltimoMensaje());
    }

    @Test
    public void testCantarFlorSinTenerFlor() {
        // Jugador 0 NO tiene 3 cartas del mismo palo
        setupCartasDisponiblesSinFlor(0);

        mano.setFloresCantadas(0);
        mano.setEsperandoRespuesta(false);

        // Al cantar FLOR, esperamos que lance excepción si el código
        // checa que no se puede cantar (pues no hay Flor).
        Exception ex = assertThrows(FlorException.class, 
            () -> manoService.cantosFlor(codigo, Cantos.FLOR));
        
        // Ajusta el mensaje a lo que realmente lanza tu método
        assertEquals("No podés cantar más veces flor/No tenés flor", ex.getMessage());
        // Aseguramos que no quedó esperando respuesta
        assertFalse(mano.getEsperandoRespuesta());
        // Aseguramos que no incrementó
        assertEquals(0, mano.getFloresCantadas());
    }

    @Test
    public void testCantarContraflorSinFlorPrevia() {
        // Jugador 0 con flor, pero todavía no la cantó => floresCantadas=0
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(0);        // Nadie ha cantado Flor
        mano.setQueFlorPuedeCantar(1); 
        mano.setEsperandoRespuesta(false);

        // Intenta directamente "CONTRAFLOR"
        EnvidoException ex = assertThrows(EnvidoException.class, 
            () -> manoService.cantosFlor(codigo, Cantos.CONTRAFLOR));

        assertEquals("No podés cantar contraflor capo", ex.getMessage());
        assertFalse(mano.getEsperandoRespuesta());
        assertEquals(0, mano.getFloresCantadas());
    }

    @Test
    public void testCantarContraflorExitoso() {
        setupCartasDisponiblesConFlor(0);

        // Se cantó Flor anteriormente => floresCantadas=1
        mano.setFloresCantadas(1);
        mano.setQueFlorPuedeCantar(2);
        mano.setEsperandoRespuesta(false);

        // Canta "CONTRAFLOR"
        manoService.cantosFlor(codigo, Cantos.CONTRAFLOR);

        // Debe incrementarse floresCantadas
        assertEquals(2, mano.getFloresCantadas());
        // Debe estar esperando respuesta
        assertTrue(mano.getEsperandoRespuesta());
        // Último mensaje
        assertEquals(Cantos.CONTRAFLOR, mano.getUltimoMensaje());
    }

    @Test
    public void testResponderFlorQuiero() {
        // Se supone que ya se cantó Flor
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(1);
        mano.setEsperandoRespuesta(true);
        // Por ejemplo, gana la Flor el equipo 0
        mano.setEquipoGanadorFlor(0);

        // Inicializamos puntajes
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);

        // Responder FLOR => QUIERO
        manoService.responderFlor(codigo, Cantos.QUIERO);

        // Verificamos que se sumaron puntos 
        // En tu código, QUIERO => se llama a gestionarPuntosFlor(false, codigo, null)
        // => valor base 6 para contraflor (o flor) => equipo 0
        // Ajusta según tu reglamento. Aquí asumimos 6 para "Flor querida".
        assertEquals(6, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        // Se deshabilita la flor
        assertFalse(mano.getPuedeCantarFlor());
        assertNull(mano.getQueFlorPuedeCantar());
        // Dejamos de esperar respuesta
        assertFalse(mano.getEsperandoRespuesta());
    }

    @Test
    public void testResponderFlorMeAchico() {
        // Se cantó Flor
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(1);
        mano.setQueFlorPuedeCantar(2); 
        mano.setEsperandoRespuesta(true);

        // El equipo respondedor = 1
        mano.setJugadorTurno(1);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);

        // CON_FLOR_ME_ACHICO => se llama a gestionarPuntosFlor(false, codigo, 4)
        manoService.responderFlor(codigo, Cantos.CON_FLOR_ME_ACHICO);

        // Revisa en tu código si sumas 4 puntos al "respondedor" o al "cantor".
        // Tu método hace: if(equipoRespondedor==0) => eq2, else => eq1
        // Aquí equipoRespondedor=1 => se suman al eq1
        assertEquals(4, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        assertFalse(mano.getEsperandoRespuesta());
        assertFalse(mano.getPuedeCantarFlor());
        assertNull(mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testResponderFlorNoQuiero() {
        // Se cantó Contraflor, p.e. floresCantadas=2
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(2);
        mano.setEsperandoRespuesta(true);
        // Jugador turno = 1 => equipoRespondedor=1 => se suman puntos al eq1
        mano.setJugadorTurno(1);

        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);

        // Llamamos NO_QUIERO
        manoService.responderFlor(codigo, Cantos.NO_QUIERO);

        // Se gestiona => gestionarPuntosFlor(false, codigo, 5)
        // => equipoRespondedor=1 => eq1 += 5
        assertEquals(5, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        assertFalse(mano.getEsperandoRespuesta());
        assertFalse(mano.getPuedeCantarFlor());
        assertNull(mano.getQueFlorPuedeCantar());
    }
}
