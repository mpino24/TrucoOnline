package es.us.dp1.lx_xy_24_25.truco_beasts.carta;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.junit.jupiter.api.extension.ExtendWith;

import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Palo;

@ExtendWith(MockitoExtension.class)
public class CartaServiceTest {
     @Mock
     private CartaRepository cartaRepository;
     
     @InjectMocks
     private CartaService cartaService;
          
     @Before
     public void setUp() {
         MockitoAnnotations.openMocks(this);
     }
     
    @Test
    public void findCartaByIdTestIsolated() { //Hecho por David
        Carta carta = new Carta();
        carta.setId(1);
        carta.setPalo(Palo.OROS);
        carta.setValor(1);
        carta.setPoder(1);
        carta.setFoto("/foto.jpg");
        Optional<Carta> cartaOptional = Optional.of(carta);
        when(cartaRepository.findById(1)).thenReturn(cartaOptional);
        
        assertEquals(carta, cartaService.findCartaById(1));
    }
    
    @Test
    public void findCartaByIdTestNotFoundIsolated() { //Hecho por David
        Optional<Carta> cartaOptional = Optional.empty();
        when(cartaRepository.findById(1)).thenReturn(cartaOptional);
        assertNull(cartaService.findCartaById(1));
    }
        
}
