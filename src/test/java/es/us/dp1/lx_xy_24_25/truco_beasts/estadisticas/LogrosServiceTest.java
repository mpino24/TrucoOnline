package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.maven.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.LogrosService;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.Logros;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.Metrica;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.LogroRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.EstadisticasService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
@AutoConfigureTestDatabase
public class LogrosServiceTest {

    @Autowired
    private LogrosService logrosService;

    @Autowired
    private LogroRepository logroRepository;

    @Autowired
    private EstadisticasService estadisticasService;

 

    @Test
    public void saveLogroTest() {
        Logros logro = new Logros();
        logro.setName("Nuevo Logro");
        logro.setDescripcion("Descripción del logro");
        logro.setValor(100);
        logro.setMetrica(Metrica.NO_QUIEROS);

        Logros savedLogro = logrosService.save(logro);

        assertNotNull(savedLogro.getId());
        assertEquals("Nuevo Logro", savedLogro.getName());
        assertEquals("Descripción del logro", savedLogro.getDescripcion());
        assertEquals(100, savedLogro.getValor());
    }

    @Test
    public void updateLogroTest() {
        Logros logro = new Logros();
        logro.setName("Logro a Actualizar");
        logro.setDescripcion("Descripción del logro");
        logro.setValor(100);
        logro.setMetrica(Metrica.NO_QUIEROS);
        logrosService.save(logro);

        logro.setDescripcion("Descripción actualizada");
        logro.setValor(200);

        Logros updatedLogro = logrosService.updateLogro(logro);

        assertEquals("Descripción actualizada", updatedLogro.getDescripcion());
        assertEquals(200, updatedLogro.getValor());
    }

    @Test
    public void findAllLogrosTest() {
        List<Logros> logros = logrosService.findAllLogros(true, null);

        assertNotNull(logros);
        assertTrue(logros.size() > 0);
    }

    @Test
    public void findAllLogrosForNonExistingPlayerTest() {
        Integer jugadorIdInexistente = 999;  
        assertThrows(ResourceNotFoundException.class, () -> logrosService.findAllLogros(false, jugadorIdInexistente));

    }

@Test
public void findLogroByMetricaTest() {
    List<Logros> foundLogro = logrosService.findLogroByMetrica(Metrica.NO_QUIEROS);

    assertNotNull(foundLogro);
}

    @Test
    public void findLogroByIdTest() {
        Logros logro = new Logros();
        logro.setName("Logro por ID");
        logro.setDescripcion("Descripción del logro");
        logro.setValor(100);
        logro.setMetrica(Metrica.NO_QUIEROS);
        logrosService.save(logro);

        Logros foundLogro = logrosService.findLogroById(logro.getId());

        assertNotNull(foundLogro);
        assertEquals("Logro por ID", foundLogro.getName());
    }

    @Test
    public void findLogroByIdNotFoundTest() {
        Integer invalidId = 999;
        
        assertThrows(ResourceNotFoundException.class, () -> logrosService.findLogroById(invalidId));
    }


    @Test
    public void deleteLogroTest() {
        Logros logro = new Logros();
        logro.setName("Logro a Eliminar");
        logro.setDescripcion("Descripción del logro");
        logro.setValor(100);
        logro.setMetrica(Metrica.NO_QUIEROS);
        logrosService.save(logro);

        logrosService.deleteLogro(logro.getId());

        assertThrows(ResourceNotFoundException.class, () -> logrosService.findLogroById(logro.getId()));
    }

    @Test
    public void deleteLogroNotFoundTest() {
        assertThrows(ResourceNotFoundException.class, () -> logrosService.deleteLogro(999)); 
    }

    @Test
    public void logrosConseguidosTest() {
        Integer jugadorId = 1;  
        List<Logros> logrosConseguidos = logrosService.logrosConseguidos(jugadorId);

        assertNotNull(logrosConseguidos);
        assertTrue(logrosConseguidos.size() > 0);
    }

    @Test
    public void logrosConseguidosJugadorNotFoundTest() {
        Integer jugadorIdInexistente = 999;  
        assertThrows(ResourceNotFoundException.class, () -> logrosService.logrosConseguidos(jugadorIdInexistente)); 

    }

    @Test
    public void tieneLogroTest() {
        Integer estadistica = 150;
        Logros logro = new Logros();
        logro.setValor(100);

        Boolean tieneLogro = logrosService.tieneLogro(estadistica, logro);

        assertTrue(tieneLogro);
    }

    @Test
    public void tieneLogroWithInvalidValueTest() {
        Integer estadistica = 50;
        Logros logro = new Logros();
        logro.setValor(100);

        Boolean tieneLogro = logrosService.tieneLogro(estadistica, logro);

        assertFalse(tieneLogro);
    }

    @Test
    public void findTotalLogrosTest() {
        Integer totalLogros = logrosService.findTotalLogros();

        assertTrue(totalLogros >= 0);
    }

    @Test
    public void findTotalLogrosTestWithEmptyDatabase() {
        logroRepository.deleteAll();

        Integer totalLogros = logrosService.findTotalLogros();

        assertEquals(0, totalLogros);
    }
}
