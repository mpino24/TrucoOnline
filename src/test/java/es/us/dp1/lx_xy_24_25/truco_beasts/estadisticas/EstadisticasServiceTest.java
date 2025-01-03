package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;



import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class EstadisticasServiceTest {

    @InjectMocks
    private EstadisticasService estadisticasService;

    @Mock
    private EstadisticasRepository estadisticasRepository;

    @Mock
    private JugadorRepository jugadorRepository;

    private EstadisticaJugador estadisticaJugador;
    private EstadisticaGlobal estadisticaGlobal;

    @BeforeEach
    public void setUp() {

        estadisticaJugador = new EstadisticaJugador();
        estadisticaJugador.setPartidasJugadas(10);
        estadisticaJugador.setVictorias(5);
        estadisticaJugador.setDerrotas(5);
        estadisticaJugador.setTiempoJugado(100);
        estadisticaJugador.setPartidasA2(3);
        estadisticaJugador.setPartidasA4(4);
        estadisticaJugador.setPartidasA6(3);
        estadisticaJugador.setNumeroFlores(2);
        estadisticaJugador.setQuieros(1);
        estadisticaJugador.setNoQuieros(1);
        estadisticaJugador.setNumeroEnganos(1);
        estadisticaJugador.setPartidasConFlor(1);

        estadisticaGlobal = new EstadisticaGlobal();
        estadisticaGlobal.setTiempoJugado(1000);
        estadisticaGlobal.setVictorias(50);
        estadisticaGlobal.setPartidasJugadas(100);
        estadisticaGlobal.setDerrotas(50);
        estadisticaGlobal.setPartidasA2(20);
        estadisticaGlobal.setPartidasA4(30);
        estadisticaGlobal.setPartidasA6(50);
        estadisticaGlobal.setNumeroFlores(100);
        estadisticaGlobal.setNumeroEnganos(30);
        estadisticaGlobal.setQuieros(50);
        estadisticaGlobal.setNoQuieros(20);
        estadisticaGlobal.setJugadoresTotales(200);
        estadisticaGlobal.setPartidasConFlor(60);
    }

    @Test
    void testGetEstadisticasJugadorJugadorNoEncontrado() {

        when(jugadorRepository.findById(4)).thenReturn(Optional.empty());  

        assertThrows(ResourceNotFoundException.class, () -> {
            estadisticasService.getEstadisticasJugador(4);  
        });
    }

    @Test
    void testGetEstadisticasJugadorJugadorEncontrado() throws ResourceNotFoundException {
        Jugador jugador= new Jugador();
        when(jugadorRepository.findById(1)).thenReturn(Optional.of(jugador));  
        when(estadisticasRepository.findAllPartidasJugadas(1)).thenReturn(10);
        when(estadisticasRepository.findVictorias(1)).thenReturn(5);
        when(estadisticasRepository.findTiempoJugado(1)).thenReturn(100);
        when(estadisticasRepository.findPartidasA2(1)).thenReturn(3);
        when(estadisticasRepository.findPartidasA4(1)).thenReturn(4);
        when(estadisticasRepository.findPartidasA6(1)).thenReturn(3);
        when(estadisticasRepository.findNumeroFlores(1)).thenReturn(2);
        when(estadisticasRepository.findQuieros(1)).thenReturn(1);
        when(estadisticasRepository.findNoQuieros(1)).thenReturn(1);
        when(estadisticasRepository.findNumeroEnganos(1)).thenReturn(1);
        when(estadisticasRepository.findPartidasConFlor(1)).thenReturn(1);

        EstadisticaJugador result = estadisticasService.getEstadisticasJugador(1);

        assertNotNull(result);
        assertEquals(10, result.getPartidasJugadas());
        assertEquals(5, result.getVictorias());
        assertEquals(5, result.getDerrotas());
        assertEquals(100, result.getTiempoJugado());
        assertEquals(3, result.getPartidasA2());
        assertEquals(4, result.getPartidasA4());
        assertEquals(3, result.getPartidasA6());
        assertEquals(2, result.getNumeroFlores());
        assertEquals(1, result.getQuieros());
        assertEquals(1, result.getNoQuieros());
        assertEquals(1, result.getNumeroEnganos());
        assertEquals(1, result.getPartidasConFlor());
    }

    @Test
    void testGetEstadisticasGlobales() {

        when(estadisticasRepository.findTiempoJugadoGlobal()).thenReturn(1000);
        when(estadisticasRepository.findVictoriasGlobal()).thenReturn(50);
        when(estadisticasRepository.findAllPartidasJugadasGlobal()).thenReturn(100);
        when(estadisticasRepository.findPartidasA2Global()).thenReturn(20);
        when(estadisticasRepository.findPartidasA4Global()).thenReturn(30);
        when(estadisticasRepository.findPartidasA6Global()).thenReturn(50);
        when(estadisticasRepository.findNumeroFloresGlobal()).thenReturn(100);
        when(estadisticasRepository.findNumeroEnganosGlobal()).thenReturn(30);
        when(estadisticasRepository.findQuierosGlobal()).thenReturn(50);
        when(estadisticasRepository.findNoQuierosGlobal()).thenReturn(20);
        when(estadisticasRepository.findJugadoresTotales()).thenReturn(200);
        when(estadisticasRepository.findPartidasConFlor()).thenReturn(60);

        EstadisticaGlobal result = estadisticasService.getEstadisticasGlobales();

        assertNotNull(result);
        assertEquals(1000, result.getTiempoJugado());
        assertEquals(50, result.getVictorias());
        assertEquals(100, result.getPartidasJugadas());
        assertEquals(50, result.getDerrotas());
        assertEquals(20, result.getPartidasA2());
        assertEquals(30, result.getPartidasA4());
        assertEquals(50, result.getPartidasA6());
        assertEquals(100, result.getNumeroFlores());
        assertEquals(30, result.getNumeroEnganos());
        assertEquals(50, result.getQuieros());
        assertEquals(20, result.getNoQuieros());
        assertEquals(200, result.getJugadoresTotales());
        assertEquals(60, result.getPartidasConFlor());
    }
}
