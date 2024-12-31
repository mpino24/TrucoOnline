package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class estadisticasServiceTest {
    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
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
