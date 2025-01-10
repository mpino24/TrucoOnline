package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.TransactionSystemException;

import es.us.dp1.lx_xy_24_25.truco_beasts.TrucoBeastsApplication;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;

@SpringBootTest(classes = TrucoBeastsApplication.class)
@AutoConfigureTestDatabase
public class TestJugadorService {

    @Autowired
    private JugadorService jugadorService;

    public TestJugadorService() {
    }
    Jugador jugador;
    Jugador jugador2;

    @BeforeEach
    public void setup() {
        jugador = jugadorService.findJugadorById(3);
        jugador2 = jugadorService.findJugadorById(4);
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testFindAmigosByUserId() {
        jugadorService.deleteFriends(jugador.getUser().getId(), jugador2.getId());
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());

        assertEquals(1, jugadorService.findAmigosByUserId(jugador.getUser().getId()).size());

        assertEquals(jugador2.getFirstName(), jugadorService.findAmigosByUserId(jugador.getUser().getId()).get(0).getFirstName());

        assertEquals(1, jugadorService.findAmigosByUserId(jugador2.getUser().getId()).size());

        assertEquals(jugador.getFirstName(), jugadorService.findAmigosByUserId(jugador2.getUser().getId()).get(0).getFirstName());
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testCheckIfAreFriends() {
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());
        assertTrue(jugadorService.checkIfAreFriends(jugador, jugador2));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testfindJugadorByUserName() {
        assertEquals(jugador.getFirstName(), jugadorService.findJugadorByUserName(jugador.getUser().getUsername()).getFirstName());
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testAddFriendAgainFallo() {
        jugadorService.deleteFriends(jugador.getUser().getId(), jugador2.getId());
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());

        assertThrows(IllegalStateException.class, () -> jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId()));
    }

    @Test
    void testAddYourselfFallo() {

        assertThrows(IllegalStateException.class, () -> jugadorService.addNewFriends(jugador.getUser().getId(), jugador.getId()));
    }

    @Test
    void testAddNotFoundFriendFallo() {
        assertThrows(ResourceNotFoundException.class, () -> jugadorService.addNewFriends(jugador.getUser().getId(), 50));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testFindJugadorByUserName() {
        assertEquals(jugador.getFirstName(), jugadorService.findJugadorByUserName(jugador.getUser().getUsername()).getFirstName());
    }

    @Test
    void testFindNotFoundJugadorByUserNameFallo() {
        assertThrows(ResourceNotFoundException.class, () -> jugadorService.findJugadorByUserName("Manolito074k"));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testDeleteFriend() {
        jugadorService.deleteFriends(jugador.getUser().getId(), jugador2.getId());

        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());
        assertEquals(1, jugadorService.findAmigosByUserId(jugador.getUser().getId()).size());

        jugadorService.deleteFriends(jugador.getUser().getId(), jugador2.getId());
        assertEquals(0, jugadorService.findAmigosByUserId(jugador.getUser().getId()).size());

    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testComprobarExistenciaSolicitud() {
        jugadorService.crearSolicitud(jugador.getUser().getId(), jugador2.getId());

        assertTrue(jugadorService.comprobarExistenciaSolicitud(jugador2, jugador));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testCrearSolicitud() {
        jugadorService.deleteSolicitud(jugador2.getUser().getId(), jugador.getId());

        jugadorService.crearSolicitud(jugador.getUser().getId(), jugador2.getId());
        assertEquals(1, jugadorService.findSolicitudesByUserId(jugador2.getUser().getId()).size());

    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testDeleteSolicitud() {
        jugadorService.deleteSolicitud(jugador2.getUser().getId(), jugador.getId());

        jugadorService.crearSolicitud(jugador.getUser().getId(), jugador2.getId());
        assertEquals(1, jugadorService.findSolicitudesByUserId(jugador2.getUser().getId()).size());

        jugadorService.deleteSolicitud(jugador2.getUser().getId(), jugador.getId());
        assertEquals(0, jugadorService.findSolicitudesByUserId(jugador.getUser().getId()).size());

    }

    @Test
    void testCrearSolicitudNotFoundPlayerFallo() {
        assertThrows(ResourceNotFoundException.class, () -> jugadorService.crearSolicitud(jugador.getUser().getId(), 50));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    @DirtiesContext
    void testFindSolicitudByUserId() {
        jugadorService.deleteSolicitud(jugador2.getUser().getId(), jugador.getId());
        jugadorService.crearSolicitud(jugador.getUser().getId(), jugador2.getId());

        assertEquals(jugador.getFirstName(), jugadorService.findSolicitudesByUserId(jugador2.getUser().getId()).get(0).getFirstName());
    }

    /* -------*/

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindAll() {
        Collection<Jugador> jugadores = jugadorService.findAll();
        assertNotNull(jugadores);
        assertTrue(jugadores.size() > 0);
    }
        

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testFindJugadorByUserId() {
        Jugador foundJugador = jugadorService.findJugadorByUserId(jugador.getUser().getId());
        assertNotNull(foundJugador);
        assertEquals(jugador.getId(), foundJugador.getId());
    }
    
    @Test
    void testFindJugadorByUserIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> jugadorService.findJugadorByUserId(999));
    }

    @Test
    void testExistsJugador() {
        assertTrue(jugadorService.existsJugador(jugador.getId()));
        assertFalse(jugadorService.existsJugador(999));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void testUpdateJugador() {
        jugador.setFirstName("Actualizado");
        jugadorService.updateJugador(jugador, jugador.getUser());
        Jugador updatedJugador = jugadorService.findJugadorById(jugador.getId());
        assertEquals("Actualizado", updatedJugador.getFirstName());
    }

    @Test
    void testUpdateJugadorNotFound() {
        Jugador jugadorNoExistente = new Jugador();
        jugadorNoExistente.setId(999);
        assertThrows(TransactionSystemException.class, () -> jugadorService.updateJugador(jugadorNoExistente, jugador.getUser()));
    }

}
