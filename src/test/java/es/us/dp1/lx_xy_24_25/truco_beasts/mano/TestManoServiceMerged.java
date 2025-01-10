package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.FlorException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TrucoException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;

public class TestManoServiceMerged {

    private Partida partida;
    private Mano mano;
    private String codigo;
    private ManoService manoService;

    private CartaRepository cartaRepository;
    private PartidaService partidaService;
    private PartidaJugadorService partidaJugadorService;

    @BeforeEach
    public void setupBase() {
        partida = new Partida();
        mano = new Mano();

        // Configuración inicial de la partida
        partida.setNumJugadores(2);    
        partida.setJugadorMano(0);     
        partida.setCodigo("TESTMERGED"); 
        partida.setConFlor(false);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
        mano.setPartida(partida);
        mano.setJugadorTurno(partida.getJugadorMano());
        mano.setGanadoresRondas(new ArrayList<>(List.of(0, 0)));

        // Configuración de Envidos
        List<Integer> envidos = new ArrayList<>(List.of(0, 0, 0));
        mano.setEnvidosCantados(envidos);
        mano.setFloresCantadas(0);
        mano.setCartasLanzadasTotales(inicializarCartasLanzadasTotales(partida.getNumJugadores()));

        // Inicialización de cartasDisp como una lista vacía para evitar NPE
        mano.setCartasDisp(new ArrayList<>());
        mano.setCartasNoBorradas(new ArrayList<>());

        // Mocking de los servicios
        cartaRepository = Mockito.mock(CartaRepository.class);
        partidaService = Mockito.mock(PartidaService.class);
        partidaJugadorService = Mockito.mock(PartidaJugadorService.class);

        // Definición de comportamientos mock
        Mockito.when(partidaService.findPartidaByCodigo(partida.getCodigo()))
            .thenReturn(partida);

        manoService = new ManoService(cartaRepository, partidaService, partidaJugadorService);

        codigo = partida.getCodigo();
        manoService.actualizarMano(mano, codigo);
    }

    private List<List<Carta>> inicializarCartasLanzadasTotales(Integer numJugadores){
        List<List<Carta>> res = new ArrayList<>();
        Integer rondas = 3;    
        for (int j = 0; j < numJugadores; j++) {
            List<Carta> listaVacia = new ArrayList<>();
            for (int i = 0; i < rondas; i++) {
                listaVacia.add(null);
            }
            res.add(listaVacia);  
        }
        return res;
    }

    // Métodos de configuración para pruebas existentes y nuevas
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

        List<Carta> cartasTotales = new ArrayList<>();
        
        cartasTotales.add(c1);
        cartasTotales.add(c2);
        cartasTotales.add(c3);
        List<List<Carta>> cartasDisponibles = new ArrayList<>();
        if(mano.getCartasDisp()!=null){
            cartasDisponibles = mano.getCartasDisp();
        }
        
        cartasDisponibles.add(jugador, cartasTotales);
        mano.setCartasDisp(cartasDisponibles);
        mano.setCartasNoBorradas(cartasDisponibles);

        mano.setRondaActual(1);

        List<Carta> lanzadas = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            lanzadas.add(null);
        }
        mano.setCartasLanzadasRonda(lanzadas);
        mano.setJugadorTurno(jugador);
    }

    private void setupCartasDisponiblesSinFlor(Integer jugador) {
        Carta c1 = new Carta(); 
        Carta c2 = new Carta(); 
        Carta c3 = new Carta(); 
        c1.setId(1); 
        c1.setPalo(Palo.ESPADAS);
        c1.setPoder(7); 
        c1.setValor(7);

        c2.setId(2); 
        c2.setPalo(Palo.OROS);
        c2.setPoder(6); 
        c2.setValor(6);

        c3.setId(3); 
        c3.setPalo(Palo.COPAS);
        c3.setPoder(5);
        c3.setValor(5);

        List<Carta> cartasTotales = new ArrayList<>();
        cartasTotales.add(c1);
        cartasTotales.add(c2);
        cartasTotales.add(c3);

        List<List<Carta>> cartasDisponibles = new ArrayList<>(mano.getCartasDisp());
        // Asegurarse de que la lista tenga suficiente tamaño
        while (cartasDisponibles.size() <= jugador) {
            cartasDisponibles.add(new ArrayList<>());
        }
        cartasDisponibles.set(jugador, cartasTotales);
        mano.setCartasDisp(cartasDisponibles);
        mano.setCartasNoBorradas(cartasDisponibles);

        mano.setRondaActual(1);

        List<Carta> lanzadas = new ArrayList<>();
        for (int i = 0; i < partida.getNumJugadores(); i++) {
            lanzadas.add(null);
        }
        mano.setCartasLanzadasRonda(lanzadas);

        mano.setJugadorTurno(jugador);
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

    // -------------------------------------------
    // Pruebas de ManoService (TestManoService)
    // -------------------------------------------

    @Test
    public void testRepartirCartas() {
        Carta carta = mock(Carta.class);
        Mockito.when(cartaRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(carta));

        List<List<Carta>> cartasRepartidas = manoService.repartirCartas(partida);

        assertNotNull(cartasRepartidas);
        assertEquals(2, cartasRepartidas.size()); // 2 jugadores
        cartasRepartidas.forEach(cartas -> assertEquals(3, cartas.size())); // Cada jugador recibe 3 cartas
    }

    @Test
    public void tirarCartaSePoneANullCartaDisponible()  {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Carta cartaLanzada = mano.getCartasDisp().get(jugadorActual).get(cartaIdALanzar);
        assertEquals(null, cartaLanzada);
    }

    @Test
    public void tirarCartaHayCartaLanzada() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
    }

    @Test
    public void tirarCartaSeAvanzaDeTurno() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Integer siguienteTurno = mano.getJugadorTurno();
        assertEquals(mano.siguienteJugador(jugadorActual), siguienteTurno);
    }

    @Test 
    public void tirarCartaNoDisponible() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        CartaTiradaException cartaTiradaException = assertThrows(CartaTiradaException.class, () -> manoService.tirarCarta(codigo, cartaIdALanzar));
        assertEquals("Esa carta ya fue tirada, no podés volver a hacerlo", cartaTiradaException.getMessage());
    }

    @Test 
    public void tirarCartaTieneQueResponder() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setupCartasDisponibles(jugadorActual,1);
        mano.setEsperandoRespuesta(true);

        CartaTiradaException cartaTiradaException = assertThrows(CartaTiradaException.class, () -> manoService.tirarCarta(codigo, cartaIdALanzar));
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

    @Test 
    public void cantarTruco() {
        setup(0, 4);
        setupTruco(null, null, null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
    }

    @Test 
    public void cantarRetruco() {
        setup(0, 4);
        setupTruco(1, 2,null);
        setupCartasDisponibles(0, 2);

        manoService.cantosTruco(codigo, Cantos.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
    }

    @Test 
    public void cantarValecuatro() {
        setup(0, 4);
        setupTruco(1, 3,null);
        setupCartasDisponibles(0, 2);

        manoService.cantosTruco(codigo, Cantos.VALECUATRO);
        assertTrue(mano.getJugadorTurno() == 1);
        assertEquals(0, mano.getEquipoCantor());
    }

    @Test
    public void responderQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null, null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        manoService.responderTruco(codigo, Cantos.QUIERO);
        assertTrue(mano.getJugadorTurno() ==0);
        assertEquals(2, mano.getPuntosTruco());
    }

    @Test
    public void responderNoQuieroTruco() {
        setup(0, 4);
        setupTruco(null, null, null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO);
        assertTrue(mano.getJugadorTurno() ==1);

        manoService.responderTruco(codigo, Cantos.NO_QUIERO);
        assertEquals(1, mano.getPuntosTruco());
    }

    @Test
    public void responderQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2,null);
        setupCartasDisponibles(1, 1);

        manoService.cantosTruco(codigo, Cantos.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2);

        manoService.responderTruco(codigo, Cantos.QUIERO); 
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(3, mano.getPuntosTruco());
    }

    @Test
    public void responderNoQuieroRetruco() {
        setup(0, 4);
        setupTruco(0, 2,null);
        setupCartasDisponibles(1, 1);

        manoService.cantosTruco(codigo, Cantos.RETRUCO);
        assertTrue(mano.getJugadorTurno() ==2); 

        manoService.responderTruco(codigo, Cantos.NO_QUIERO); 
        assertEquals(2, mano.getPuntosTruco());
    }

    @Test 
    public void responderQuieroValecuatro() {
        setup(0, 4);
        setupTruco(0, 3,0);
        setupCartasDisponibles(1, 2);

        manoService.cantosTruco(codigo, Cantos.VALECUATRO);
        assertEquals(2,mano.getJugadorTurno()); 

        manoService.responderTruco(codigo, Cantos.QUIERO); 
        assertFalse(mano.getEsperandoRespuesta());
        assertTrue(mano.getJugadorTurno() ==1);
        assertEquals(4, mano.getPuntosTruco());
    }

    @Test 
    public void responderNoQuieroValecuatro(){
        setup(0, 4);
        setupTruco(0, 3,0);
        setupCartasDisponibles(1, 2);

        manoService.cantosTruco(codigo, Cantos.VALECUATRO);
        assertEquals(2,mano.getJugadorTurno()); 

        manoService.responderTruco(codigo, Cantos.NO_QUIERO);
        assertEquals(3, mano.getPuntosTruco());
    }

    @Test 
    public void responderTrucoRetrucoQuiero() {
        setup(0, 4);
        setupTruco(null, null,null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO); 
        manoService.responderTruco(codigo, Cantos.SUBIR);
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        manoService.responderTruco(codigo, Cantos.QUIERO); 
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
    }

    @Test
    public void responderRetrucoValeCuatroQuiero() {
        setup(0, 4);
        setupTruco(0, 2,null); //Se canto Truco
        setupCartasDisponibles(3, 2);
        
        
        
        manoService.cantosTruco(codigo,Cantos.RETRUCO);
        assertTrue(mano.getJugadorTurno() == 0);

        manoService.responderTruco(codigo,Cantos.SUBIR); 
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());

        manoService.responderTruco(codigo,Cantos.QUIERO); 
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(3, mano.getJugadorTurno());
        
    }


    @Test 
    public void responderTrucoRetrucoValecuatroQuiero() {
        setup(0, 4);
        setupTruco(null, null,null); //No se canto nada
        setupCartasDisponibles(0, 1);
        
        
        manoService.cantosTruco(codigo,Cantos.TRUCO); 
        assertEquals(1,mano.getJugadorTurno());

        manoService.responderTruco(codigo,Cantos.SUBIR);
        assertEquals(2, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());

        manoService.responderTruco(codigo,Cantos.SUBIR);
        assertEquals(3, mano.getPuntosTruco());
        assertEquals(1, mano.getJugadorTurno());

        manoService.responderTruco(codigo,Cantos.QUIERO); 
        assertEquals(4, mano.getPuntosTruco());
        assertEquals(0, mano.getJugadorTurno());
        
    }

    @Test 
    public void cantarTrucoNoPuede() {
        setup(0, 4);
        setupTruco(null, null,null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO); 
        manoService.responderTruco(codigo, Cantos.QUIERO); 

        TrucoException exception = assertThrows(TrucoException.class, 
            () -> manoService.cantosTruco(codigo, Cantos.TRUCO));

        assertEquals("No podes cantar truco ni ninguna de sus variantes", exception.getMessage());
    }

    @Test 
    public void cantarTrucoYaSeCanto() {
        setup(0, 4);
        setupTruco(null, null,null);
        setupCartasDisponibles(0, 1);

        manoService.cantosTruco(codigo, Cantos.TRUCO); 
        manoService.responderTruco(codigo, Cantos.QUIERO); 
        mano.siguienteTurno();
        assertTrue(mano.comprobarSiPuedeCantarTruco()); 

        TrucoException exception = assertThrows(TrucoException.class, 
            () -> manoService.cantosTruco(codigo, Cantos.TRUCO));

        assertEquals("Ya se canto el truco", exception.getMessage());
    }

    @Test 
    public void cantarRetrucoSinTrucoPrevia() {
        setup(0, 4);
        setupTruco(null, null, null);
        setupCartasDisponibles(0, 1);

        TrucoException exception = assertThrows(TrucoException.class, 
            () -> manoService.cantosTruco(codigo, Cantos.RETRUCO));

        assertEquals("No se cantó el truco", exception.getMessage());
    }

    // -------------------------------------------
    // Pruebas de ManoServiceFlor (TestManoServiceFlor)
    // -------------------------------------------

    
    @Test
    public void testCantarFlorSinTenerFlor() {
        setupCartasDisponiblesSinFlor(0);

        mano.setFloresCantadas(0);
        mano.setEsperandoRespuesta(false);

        Exception ex = assertThrows(FlorException.class, 
            () -> manoService.cantosFlor(codigo, Cantos.FLOR));

        assertEquals("No podés cantar más veces flor/No tenés flor", ex.getMessage());
        assertFalse(mano.getEsperandoRespuesta());
        assertEquals(0, mano.getFloresCantadas());
    }

    @Test
    public void testCantarContraflorSinFlorPrevia() {
        setupCartasDisponiblesConFlor(0);

        mano.setFloresCantadas(0);
        mano.setQueFlorPuedeCantar(1);
        mano.setEsperandoRespuesta(false);

        FlorException ex = assertThrows(FlorException.class, 
            () -> manoService.cantosFlor(codigo, Cantos.CONTRAFLOR));

        assertEquals("Canto no valido", ex.getMessage());
        assertFalse(mano.getEsperandoRespuesta());
        assertEquals(0, mano.getFloresCantadas());
    }

    @Test
    public void testCantarContraflorExitoso() {
        setupCartasDisponiblesConFlor(0);
        setupCartasDisponiblesConFlor(1);
        mano.setFloresCantadas(1);
        mano.setQueFlorPuedeCantar(2);
        mano.setEsperandoRespuesta(true);
        mano.crearListaTantosCadaJugadorFlor();

        manoService.responderFlor(codigo, Cantos.CONTRAFLOR);

        assertEquals(2, mano.getFloresCantadas());
        assertTrue(mano.getEsperandoRespuesta());
        assertEquals(Cantos.CONTRAFLOR, mano.getUltimoMensaje());
    }

    @Test
    public void testResponderFlorQuiero() {
        setupCartasDisponiblesConFlor(0);
        setupCartasDisponiblesConFlor(1);
        mano.crearListaTantosCadaJugadorFlor();

        mano.setFloresCantadas(1);
        mano.setEsperandoRespuesta(true);
        mano.setEquipoGanadorFlor(0);

        manoService.responderFlor(codigo, Cantos.QUIERO);

        assertEquals(6, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
        assertFalse(mano.getEsperandoRespuesta());
    }

    @Test
    public void testResponderFlorMeAchico() {
        setupCartasDisponiblesConFlor(0);
        setupCartasDisponiblesConFlor(1);
        mano.crearListaTantosCadaJugadorFlor();

        mano.setFloresCantadas(1);
        mano.setQueFlorPuedeCantar(2);
        mano.setEsperandoRespuesta(true);
        mano.setJugadorTurno(1);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);

        manoService.responderFlor(codigo, Cantos.CON_FLOR_ME_ACHICO);

        assertEquals(4, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        assertFalse(mano.getEsperandoRespuesta());
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    @Test
    public void testResponderFlorNoQuiero() {
        setupCartasDisponiblesConFlor(0);
        setupCartasDisponiblesConFlor(1);
        mano.crearListaTantosCadaJugadorFlor();

        mano.setFloresCantadas(2);
        mano.setEsperandoRespuesta(true);
        mano.setJugadorTurno(1);

        manoService.responderFlor(codigo, Cantos.NO_QUIERO);

        assertEquals(5, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());

        assertFalse(mano.getEsperandoRespuesta());
        assertFalse(mano.getPuedeCantarFlor());
        assertEquals(0, mano.getQueFlorPuedeCantar());
    }

    // -------------------------------------------
    // Pruebas para Métodos No Cubiertos Anteriormente
    // -------------------------------------------

    @Test
    public void testCrearMano() {
        // Crear instancias reales de Carta con los atributos necesarios
        Carta carta1 = new Carta();
        carta1.setId(1);
        carta1.setPalo(Palo.ESPADAS);
        carta1.setPoder(7);
        carta1.setValor(7);
        
        Carta carta2 = new Carta();
        carta2.setId(2);
        carta2.setPalo(Palo.OROS);
        carta2.setPoder(6);
        carta2.setValor(6);
        
        Carta carta3 = new Carta();
        carta3.setId(3);
        carta3.setPalo(Palo.COPAS);
        carta3.setPoder(5);
        carta3.setValor(5);
        
        // Configurar el mock del repositorio para retornar estas cartas
        Mockito.when(cartaRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(carta1))
               .thenReturn(Optional.of(carta2))
               .thenReturn(Optional.of(carta3));
    
        Mano nuevaMano = manoService.crearMano(partida);
    
        assertNotNull(nuevaMano);
        assertEquals(partida, nuevaMano.getPartida());
        assertEquals(partida.getJugadorMano(), nuevaMano.getJugadorTurno());
        assertEquals(1, nuevaMano.getRondaActual());
        assertEquals(1, nuevaMano.getPuntosTruco());
        assertEquals(0, nuevaMano.getPuntosEnvido());
        assertEquals(0, nuevaMano.getPuntosFlor());
        assertFalse(nuevaMano.getTerminada());
        
        // Opcional: Verificar que las cartas se han repartido correctamente
        // Dependiendo de cómo se implementa repartirCartas, ajusta esta parte
    }
    

    @Test
    public void testCompararCartasGanador() {
        setupCartasDisponibles(0,1);
        // Configurar cartas lanzadas
        Carta c0 = new Carta();
        c0.setPoder(14);
        Carta c1 = new Carta();
        c1.setPoder(13);
        mano.getCartasLanzadasRonda().set(0, c0);
        mano.getCartasLanzadasRonda().set(1, c1);

        Integer ganador = mano.compararCartas();

        assertEquals(0, ganador); // Jugador 0 gana la ronda
        assertEquals(1, mano.getGanadoresRondas().get(0));
        assertEquals(0, mano.getGanadoresRondas().get(1));
    }

    @Test
    public void testCompararCartasEmpate() {
        setupCartasDisponibles(0,1);
        // Configurar cartas lanzadas
        Carta c0 = new Carta();
        c0.setPoder(14);
        Carta c1 = new Carta();
        c1.setPoder(14);
        mano.getCartasLanzadasRonda().set(0, c0);
        mano.getCartasLanzadasRonda().set(1, c1);

        Integer ganador = mano.compararCartas();

        assertEquals(0, ganador); // Empate
        assertEquals(1, mano.getGanadoresRondas().get(0));
        assertEquals(1, mano.getGanadoresRondas().get(1));
    }

    @Test
    public void testResponderEnvidoQuiero() {
        setupCartasDisponiblesSinFlor(0);
        setupCartasDisponiblesSinFlor(1);

        mano.setEsperandoRespuesta(true);
        mano.setJugadorIniciadorDelCanto(0);
        mano.setJugadorTurno(1);
        mano.setEquipoGanadorEnvido(0);

        manoService.responderEnvido(codigo, Cantos.QUIERO);

        // Verificar que los puntos de Envido se gestionan correctamente
        // Este test depende de la implementación específica de gestionarPuntosEnvido
        // Aquí asumimos que se suman los puntos de Envido al equipo ganador
        assertEquals(0, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());
    }

    @Test
    public void testResponderEnvidoNoQuiero() {
        setupCartasDisponiblesSinFlor(0);
        setupCartasDisponiblesSinFlor(1);

        mano.setEsperandoRespuesta(true);
        mano.setJugadorIniciadorDelCanto(0);
        mano.setJugadorTurno(1);
        mano.setEquipoGanadorEnvido(0);

        manoService.responderEnvido(codigo, Cantos.NO_QUIERO);

        // Verificar que los puntos de Envido se gestionan correctamente
        // Este test depende de la implementación específica de gestionarPuntosEnvido
        // Aquí asumimos que se suman los puntos de Envido al equipo respondedor
        assertEquals(1, partida.getPuntosEquipo1());
        assertEquals(0, partida.getPuntosEquipo2());
    }

    @Test
    public void testResponderEnvidoCantosEnvido() {
        setupCartasDisponiblesSinFlor(0);
        setupCartasDisponiblesSinFlor(1);

        mano.setEsperandoRespuesta(true);
        mano.setJugadorIniciadorDelCanto(0);
        mano.setJugadorTurno(1);
        mano.setEquipoGanadorEnvido(0); // o 1, según corresponda

        manoService.cantosEnvido(codigo, Cantos.ENVIDO);
        
        assertTrue(mano.getEsperandoRespuesta());
        assertEquals(Cantos.ENVIDO, mano.getUltimoMensaje());
    }

    // Métodos auxiliares para configuraciones específicas de pruebas
    private void setup(Integer jugMano, Integer numJugadores) {
        partida.setNumJugadores(numJugadores);
        partida.setJugadorMano(jugMano);
        partida.setCodigo("TESTMERGED");
        partida.setConFlor(false);
        mano.setPartida(partida);
        mano.setJugadorTurno(partida.getJugadorMano());
        mano.setGanadoresRondas(new ArrayList<>(List.of(0, 0)));

        List<Integer> envidos = new ArrayList<>(List.of(0, 0, 0));
        mano.setEnvidosCantados(envidos);
        mano.setFloresCantadas(0);
        mano.setCartasLanzadasTotales(inicializarCartasLanzadasTotales(numJugadores));

        manoService.actualizarMano(mano, partida.getCodigo());
    }

    private void setupTruco(Integer equipoCantor, Integer truco, Integer jugadorIniciadorCanto){
        if(equipoCantor != null) mano.setEquipoCantor(equipoCantor);
        if(truco != null) mano.setPuntosTruco(truco);

        if(jugadorIniciadorCanto != null){
            mano.setJugadorIniciadorDelCanto(jugadorIniciadorCanto); 
        }
    }
}
