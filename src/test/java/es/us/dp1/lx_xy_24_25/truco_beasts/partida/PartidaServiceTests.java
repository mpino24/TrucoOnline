package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AccessDeniedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.CodigoDuplicatedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import es.us.dp1.lx_xy_24_25.truco_beasts.util.EntityUtils;
import jakarta.transaction.Transactional;

@SpringBootTest
//@DataJpaTest(includeFilters= @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes=PartidaService.class))
@AutoConfigureTestDatabase
public class PartidaServiceTests {
    
    @Autowired
    private PartidaService partidaService;

    @Autowired
    private UserService userService;

    @Autowired
    private PartidaJugadorRepository partidaJugadorRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private JugadorService jugadorService;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;
    private Jugador jugador4;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private PartidaJugador partidaJugador;
    private PartidaJugador partidaJugador1;
    private PartidaJugador partidaJugador2;
    private PartidaJugador partidaJugador3;
    private PartidaJugador partidaJugador4;
    private Partida partida1;
    private Partida partida2;
    private PartidaJugadorView partidaJugadorView1;
    private PartidaJugadorView partidaJugadorView2;
    private PartidaJugadorView partidaJugadorView3;
    private PartidaJugadorView partidaJugadorView4;


    private final Pageable pageable= PageRequest.of(0,5,Sort.by(
        Order.asc("instanteInicio"),
        Order.desc("id")
        ));



    @Test
    public void devuelvePartidasActivas() {
        Partida partida = partidaService.findPartidaByCodigo("WWWWW");
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);
        List<Partida> partidas =this.partidaService.findAllPartidasActivas(pageable).getContent();
        Partida p1 = EntityUtils.getById(partidas, Partida.class, 0);
        assertEquals("WWWWW", p1.getCodigo());
    }

    @Test
    public void noDevuelvePartidasNoActivas() {
        List<Partida> partidas = this.partidaService.findAllPartidasActivas(pageable).getContent();
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
		int initialCount = partidaService.findAllPartidasActivas(pageable).getContent().size();

		Partida partida = new Partida();
		partida.setCodigo("TESTS");
        partida.setNumJugadores(2);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
		partida.setConFlor(true);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        partida.setPuntosMaximos(15);
		this.partidaService.savePartida(partida);

		int finalCount = partidaService.findAllPartidasActivas(pageable).getContent().size();

		assertEquals(initialCount + 1, finalCount);
		assertNotNull(partida.getId());
	}


    //hecho por Paula
    @Test
    public void comprobarFinPartidaPartidaTerminada() {
        
        Partida partida = new Partida();
        partida.setPuntosEquipo1(15);  
        partida.setPuntosEquipo2(10);
        partida.setPuntosMaximos(15);

        
        Boolean resultado = partidaService.comprobarFinPartida(partida);

        
        assertEquals(true, resultado);
        assertNotNull(partida.getInstanteFin()); 
    }

    //hecho por Paula
    @Test
    public void comprobarFinPartidaPartidaNoTerminada() {
        Partida partida = new Partida();
        partida.setPuntosEquipo1(10);  
        partida.setPuntosEquipo2(8);
        partida.setPuntosMaximos(15);

        Boolean resultado = partidaService.comprobarFinPartida(partida);

       
        assertEquals(false, resultado);
        assertNull(partida.getInstanteFin());  
    }

    //hecho por Paula
    @Test
    @Transactional
    public void devuelveTodasLasPartidas() {
        List<Partida> partidas = partidaService.findAllPartidas();
        assertNotNull(partidas);
        assertTrue(partidas.size() > 0);  
    }

    //hecho por Paula
    @Test
    public void noDevuelvePartidaPorIdInvalido() {
        assertEquals(null, partidaService.findPartidaById(9999));
    }

    //hecho por Paula
    @Test
    public void noDevuelvePartidaPorCodigoInvalido() {
        assertEquals(null,partidaService.findPartidaByCodigo("CODIGO_INEXISTENTE")  
        );
    }

    //hecho por Paula
    @Test
    @Transactional
    public void updatePartidaCorrectamente() {
        
        Partida partida = new Partida();
        partida.setCodigo("PARTIDA8");
        partida.setNumJugadores(2);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        partida.setPuntosMaximos(15);
        partida = partidaService.savePartida(partida);

        
        partida.setPuntosEquipo1(10);
        partida.setPuntosEquipo2(5);
        Partida partidaActualizada = partidaService.updatePartida(partida, partida.getId());

        
        assertEquals(10, partidaActualizada.getPuntosEquipo1());
        assertEquals(5, partidaActualizada.getPuntosEquipo2());
    }


    //hecho por Paula
    @Test
    @Transactional
    public void eliminaPartidaCorrectamente() {
        Partida partida = new Partida();
        partida.setCodigo("PARTIDA9");
        partida.setConFlor(false);
        partida.setNumJugadores(2);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        partida.setPuntosMaximos(15);
        partida = partidaService.savePartida(partida);

        int initialCount = partidaService.findAllPartidas().size();

        partidaService.deletePartida(partida.getCodigo());

        int finalCount = partidaService.findAllPartidas().size();
        assertEquals(initialCount - 1, finalCount);
    }

    public void setUpPartida(){
        jugador1 = jugadorService.findJugadorById(2);
        user1= userService.findUser(2);
        user1.setLastConnection(LocalDateTime.now());
        
        partida1 = new Partida();
        partida1.setCodigo("PARTIDA10");
        partida1.setNumJugadores(2);
        partida1.setPuntosEquipo1(0);
        partida1.setPuntosEquipo2(0);
        partida1.setVisibilidad(Visibilidad.PUBLICA);
        partida1.setPuntosMaximos(15);
        partida1.setConFlor(true); 
        partida1 = partidaService.savePartida(partida1);
        
        partidaJugador = new PartidaJugador();
        partidaJugador.setGame(partida1);
        partidaJugador.setPosicion(0);
        partidaJugador.setIsCreator(true);  
        partidaJugador.setPlayer(jugador1);
        partidaJugadorRepository.save(partidaJugador);
    }

    //hecho por Paula
    @Test
    @Transactional
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    public void startGameCorrectamente() {
    
        setUpPartida();
    
     
        partidaService.startGame(partida1.getCodigo());
    
       
        assertNotNull(partida1.getInstanteInicio()); 
        assertEquals(0, partida1.getPuntosEquipo1()); 
        assertEquals(0, partida1.getPuntosEquipo2()); 
        assertEquals(2, partida1.getNumJugadores());  
    }

    //hecho por Paula
    @Test
    @Transactional
    @WithMockUser(username = "player2", roles = {"PLAYER"})
    public void noIniciaPartidaSiUsuarioNoEsCreador() {

        setUpPartida(); 
        assertThrows(AccessDeniedException.class, () -> partidaService.startGame(partida1.getCodigo()));
    }



    //hecho por Paula
    @Test
    @Transactional
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    public void getGameCreatorCorrectamente() {

       setUpPartida();

        User gameCreator = partidaService.getGameCreator(partida1);
        assertNotNull(gameCreator);
        assertEquals("player1", gameCreator.getUsername());
    }
    //hecho por Paula
    @Test
    @Transactional
    public void noEncuentraCreadorPartida() {
        partida1 = new Partida();
        partida1.setCodigo("PARTIDA10");
        partida1.setNumJugadores(2);
        partida1.setPuntosEquipo1(0);
        partida1.setPuntosEquipo2(0);
        partida1.setVisibilidad(Visibilidad.PUBLICA);
        partida1.setPuntosMaximos(15);
        partida1.setConFlor(true); 
        partida1 = partidaService.savePartida(partida1); 


        PartidaJugador partidaJugadorNueva = new PartidaJugador();
        partidaJugadorNueva.setIsCreator(false);
        partidaJugadorNueva.setGame(partida1);

        
        assertThrows(ResourceNotFoundException.class, () -> partidaService.getGameCreator(partida1));
    }

    //hecho por Paula
    @Test
    @Transactional
    public void testFindPartidasYParticipantesCorrectamente() {

        Pageable pageable = PageRequest.of(0, 2);

        Page<PartidaDTO> partidasPage = partidaService.findPartidasYParticipantes(pageable);

        assertNotNull(partidasPage);
        assertEquals(2, partidasPage.getContent().size());
        assertEquals("WWWWW", partidasPage.getContent().get(0).getCodigo());
        assertEquals("ABCDE", partidasPage.getContent().get(1).getCodigo());
    }

    //hecho por Paula
    @Test
    @Transactional
    public void testFindPartidasYParticipantesSinPartidas() {
        partidaJugadorRepository.deleteAll();
        partidaRepository.deleteAll();
        Pageable pageable = PageRequest.of(0, 2);
        Page<PartidaDTO> partidasPage = partidaService.findPartidasYParticipantes(pageable);
        assertNotNull(partidasPage);
        assertTrue(partidasPage.getContent().isEmpty());
    }

    //hecho por Paula
    @ParameterizedTest
    @CsvSource({"2,2", "5,4", "1,1"})
    @Transactional
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    public void testFindHistorialDePartidasCorrectamente(int pagesize, int resultado) {
        
        Pageable pageable = PageRequest.of(0, pagesize);

        Page<PartidaDTO> historial = partidaService.findHistorialDePartidas(2, pageable);

        assertNotNull(historial);
        assertEquals(resultado, historial.getContent().size());
        assertEquals("ABCDE", historial.getContent().get(0).getCodigo());
    }

    //hecho por Paula
    @Test
    @Transactional
    public void testFindHistorialDePartidasSinPartidas() {
        jugador1 = jugadorService.findJugadorById(10);
        Pageable pageable = PageRequest.of(0, 2);
        Page<PartidaDTO> historial = partidaService.findHistorialDePartidas(jugador1.getId(), pageable);
        assertNotNull(historial);
        assertEquals(0, historial.getContent().size());
}

    //hecho por Paula
    @Test
    public void testConvertidorDTOsConPartidas() {

        partida1 = partidaRepository.findById(1).get();
        partida2= partidaRepository.findById(2).get();
        List<Partida> partidas = Arrays.asList(partida1, partida2);
        
        
        Pageable pageable = PageRequest.of(0, 10);
        Page<PartidaDTO> resultado = partidaService.convertidorDTOs(pageable, partidas, "player4");

        assertNotNull(resultado);
        assertEquals(2, resultado.getTotalElements());  
        assertEquals("NO", resultado.getContent().get(0).getCreador());  
        assertEquals("NO", resultado.getContent().get(1).getCreador());  
        assertEquals("Terminada", resultado.getContent().get(0).getTipo());  
        assertEquals("Terminada", resultado.getContent().get(1).getTipo());  
    }

    @Test
    @WithMockUser(username = "player8", roles = {"PLAYER"})
    public void testConvertidorDTOsSinPartidas() {
        List<Partida> partidas = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 2);
        Page<PartidaDTO> resultado = partidaService.convertidorDTOs(pageable, partidas, "player8");

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
        assertEquals(0, resultado.getTotalElements());
        assertEquals(0, resultado.getTotalPages());
    }














    
    










    
}
