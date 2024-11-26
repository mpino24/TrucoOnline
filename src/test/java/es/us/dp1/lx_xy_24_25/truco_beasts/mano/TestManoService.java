package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.carta.CartaRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CartaTiradaException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;




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


   
    


    @Test
    public void testRepartirCartas() {
       setup(0,4);
            
        CartaRepository cartaRepository = Mockito.mock(CartaRepository.class);
        Carta carta = mock(Carta.class);

        when(cartaRepository.findById(anyInt())).thenReturn(Optional.of(carta));
        
        manoService = new ManoService(cartaRepository);
    
        List<List<Carta>> cartasRepartidas = manoService.repartirCartas(partida);
    
            
        assertNotNull(cartasRepartidas);
        assertEquals(4, cartasRepartidas.size()); // 4 jugadores
        cartasRepartidas.forEach(cartas -> assertEquals(3, cartas.size())); // Cada jugador recibe 3 cartas
    
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

    @Test
    public void tirarCartaSePoneANullCartaDisponible()  {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Carta cartaLanzada = mano.getCartasDisp().get(jugadorActual).get(cartaIdALanzar);
        assertEquals(null, cartaLanzada);
        
    }
    @Test
    public void tirarCartaHayCartaLanzada() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        
        Boolean hayCartaLanzada = mano.getCartasLanzadasRonda().get(jugadorActual) != null;
        assertTrue(hayCartaLanzada);
        
    }
    @Test
    public void tirarCartaSeAvanzaDeTurno() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        Integer siguienteTurno = mano.getJugadorTurno();
        assertEquals(mano.siguienteJugador(jugadorActual), siguienteTurno);
    }

    @Test 
    public void tirarCartaNoDisponible() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);

        manoService.tirarCarta(codigo, cartaIdALanzar);
        CartaTiradaException cartaTiradaException=assertThrows(CartaTiradaException.class,()-> manoService.tirarCarta(codigo, cartaIdALanzar));
        assertEquals("Esa carta ya fue tirada, no podés volver a hacerlo", cartaTiradaException.getMessage());
    }
    @Test 
    public void tirarCartaTieneQueResponder() {
        Integer jugadorActual = 0;
        Integer cartaIdALanzar = 0;
        setup(0,4);
        setupCartasDisponibles(jugadorActual,1);
        mano.setEsperandoRespuesta(true);
       
        CartaTiradaException cartaTiradaException =assertThrows(CartaTiradaException.class,()-> manoService.tirarCarta(codigo, cartaIdALanzar));
        assertEquals("Tenés que responder antes de poder tirar una carta", cartaTiradaException.getMessage());
    }


}
