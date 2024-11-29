package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.TrucoBeastsApplication;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;

@SpringBootTest(classes = TrucoBeastsApplication.class)
@AutoConfigureTestDatabase
@Transactional
public class PartidaJugadorServiceTest {

    @Autowired
    private PartidaJugadorService pjService;

    @Autowired
    private PartidaService partidaService;

    public PartidaJugadorServiceTest() {
    }
    public Partida partida;

    @BeforeEach
    public void setUp() {
        partida = partidaService.findPartidaById(0);
        List<PartidaJugadorDTO> jugadoresConectados = pjService.getPlayersConnectedTo(partida.getCodigo());
        jugadoresConectados.stream().filter(pj -> pj.getIsCreator() == false).forEach(pj -> pjService.eliminateJugadorPartida(pj.getPlayer().getId()));

    }

    @Test
    public void getNumJugadoresInPartida() {
        Integer res = pjService.getNumJugadoresInPartida(0);
        assertTrue(res.equals(1));
    }

    @Test
    public void getNumberOfGamesConnected() {
        partida.setInstanteFin(LocalDateTime.now());
        partidaService.savePartida(partida);
        Integer res = pjService.getNumberOfGamesConnected(9);
        assertTrue(res.equals(0));

        partida.setInstanteFin(null);
        partidaService.savePartida(partida);

        res = pjService.getNumberOfGamesConnected(9);
        assertTrue(res.equals(1));

    }

    @Test
    public void getMiPosicion() {
        Integer res = pjService.getMiPosicion(9, 0);
        assertTrue(res.equals(0));

        assertThrows(ResourceNotFoundException.class, () -> pjService.getMiPosicion(10, 0));

    }

    @Test
    @WithMockUser(username = "player7", authorities = "PLAYER")
    public void addJugadorPartida() {
        try {
            partida.setInstanteFin(null);
            partida.setInstanteInicio(null);
            partidaService.savePartida(partida);

            pjService.addJugadorPartida(partida, 10, false);

            Integer res = pjService.getNumJugadoresInPartida(0);
            assertTrue(res.equals(2));

            res = pjService.getNumberOfGamesConnected(10);
            assertTrue(res.equals(1));
        } catch (AlreadyInGameException e) {
            fail("La excepción AlreadyInGameException no debería haberse lanzado.");
        }
    }

    @Test
    @WithMockUser(username = "player6", authorities = "PLAYER")
    public void eliminateJugadorPartida() {
        try {
            partida.setInstanteFin(null);
            partida.setInstanteInicio(null);
            partidaService.savePartida(partida);

            pjService.addJugadorPartida(partida, 10, false);

            Integer res = pjService.getNumJugadoresInPartida(0);
            assertTrue(res.equals(2));

            pjService.eliminateJugadorPartida(10);
            res = pjService.getNumberOfGamesConnected(10);
            assertTrue(res.equals(0));

        } catch (AlreadyInGameException e) {
            fail("La excepción AlreadyInGameException no debería haberse lanzado.");

        }
    }

    @Test
    public void eliminateJugadorNoLogged() {
        assertThrows(ResourceNotFoundException.class, () -> pjService.eliminateJugadorPartida(9));
    }

    @Test
    @WithMockUser(username = "player6", authorities = "PLAYER")
    public void eliminateMyselfPartida() {
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);

        pjService.eliminateJugadorPartida(null);
        Integer res = pjService.getNumberOfGamesConnected(9);
        assertTrue(res.equals(0));

    }

    @Test
    public void getPlayersConnectedTo() {
        List<PartidaJugadorDTO> res = pjService.getPlayersConnectedTo(partida.getCodigo());
        assertTrue(res.size() == 1);
    }

    @Test
    public void getPartidaOfUserId() {
        Partida res = pjService.getPartidaOfUserId(9);
        assertTrue(res.getCodigo().equals(partida.getCodigo()));
    }

    @Test
    public void getPartidaOfBadUserId() {
        assertThrows(ResourceNotFoundException.class, () -> pjService.getPartidaOfUserId(50));
    }

    @Test
    @WithMockUser(username = "player7", authorities = "PLAYER")
    public void changeTeamOfUser() {
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partida.setNumJugadores(4);
        partidaService.savePartida(partida);
        try {
            pjService.addJugadorPartida(partida, 10, false);

            List<PartidaJugadorDTO> jugadoresConectados = pjService.getPlayersConnectedTo(partida.getCodigo());
            assertEquals(jugadoresConectados.size(), 2);

            PartidaJugadorDTO jugador2 = jugadoresConectados.get(1);
            assertEquals(jugador2.getEquipo(), Equipo.EQUIPO2);

            pjService.changeTeamOfUser(jugador2.getPlayer().getId());

            jugadoresConectados = pjService.getPlayersConnectedTo(partida.getCodigo());
            jugador2 = jugadoresConectados.get(1);
            assertEquals(jugador2.getEquipo(), Equipo.EQUIPO1);

        } catch (AlreadyInGameException | TeamIsFullException e) {
            fail("La excepción " + e + "no debería haberse lanzado.");
        }
    }

    @Test
    public void changeTeamOfUserFailure() {
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partida.setNumJugadores(2);
        partidaService.savePartida(partida);
        try {
            pjService.addJugadorPartida(partida, 10, false);
            List<PartidaJugadorDTO> jugadoresConectados = pjService.getPlayersConnectedTo(partida.getCodigo());
            assertEquals(jugadoresConectados.size(), 2);

            PartidaJugadorDTO jugador2 = jugadoresConectados.get(1);
            assertEquals(jugador2.getEquipo(), Equipo.EQUIPO2);

            assertThrows(TeamIsFullException.class, () -> pjService.changeTeamOfUser(jugador2.getPlayer().getId()));

        } catch (AlreadyInGameException e) {
            fail("La excepción " + e + "no debería haberse lanzado.");
        }
    }

    @Test
    public void getGameCreator() {
        User res = pjService.getGameCreator(partida);
        assertTrue(res.getId().equals(9));
    }

}
