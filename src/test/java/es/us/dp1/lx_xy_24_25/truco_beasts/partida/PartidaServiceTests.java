package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectRetrievalFailureException;

import es.us.dp1.lx_xy_24_25.truco_beasts.util.EntityUtils;
import jakarta.transaction.Transactional;

@SpringBootTest
//@DataJpaTest(includeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes=PartidaService.class))
@AutoConfigureTestDatabase
public class PartidaServiceTests {
    
    @Autowired
    private PartidaService partidaService;







    @Test
    public void devuelvePartidasActivas() {
        Partida partida = partidaService.findPartidaByCodigo("WWWWW");
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);
        List<Partida> partidas = (List<Partida>) this.partidaService.findAllPartidasActivas();
        Partida p1 = EntityUtils.getById(partidas, Partida.class, 0);
        assertEquals("WWWWW", p1.getCodigo());
    }

    @Test
    public void noDevuelvePartidasNoActivas() {
        List<Partida> partidas = (List<Partida>) this.partidaService.findAllPartidasActivas();
        assertThrows(ObjectRetrievalFailureException.class, () -> EntityUtils.getById(partidas, Partida.class, 2));
    }

    @Test
    public void devuelvePartidaPorId() {
        Partida partida = this.partidaService.findPartidaById(0);
        assertEquals("WWWWW", partida.getCodigo());
    }

    @Test
    public void devuelvePartidaPorCodigo() {
        Partida partida = this.partidaService.findPartidaByCodigo("WWWWW");
        assertEquals("WWWWW", partida.getCodigo());
    }

    @Test
	@Transactional
	public void guardaPartidaConIdNuevo() {
		int initialCount = partidaService.findAllPartidasActivas().size();

		Partida partida = new Partida();
		partida.setCodigo("TESTS");
        partida.setNumJugadores(2);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
		partida.setConFlor(true);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        partida.setPuntosMaximos(15);
		this.partidaService.savePartida(partida);

		int finalCount = partidaService.findAllPartidasActivas().size();

		assertEquals(initialCount + 1, finalCount);
		assertNotNull(partida.getId());
	}

    
}
