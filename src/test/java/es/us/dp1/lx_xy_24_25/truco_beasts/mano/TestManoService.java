package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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



}
