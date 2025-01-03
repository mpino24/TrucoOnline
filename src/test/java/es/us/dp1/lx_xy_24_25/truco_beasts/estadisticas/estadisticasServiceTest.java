package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EstadisticasServiceTest {
    @Autowired
    private EstadisticasService estadisticasService;

    @Mock
    private EstadisticasRepository estadisticasRepository;


    @Test
    void testGetEstadisticasJugador() {

        Integer jugadorId = 1; // Usa el ID del jugador que generaste.

        // Act: Llama al método que deseas probar.
        EstadisticaJugador result = estadisticasService.getEstadisticasJugador(jugadorId);

        // Assert: Verifica los resultados.
        assertNotNull(result);
    }    
    
}
