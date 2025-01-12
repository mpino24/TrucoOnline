package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.TrucoBeastsApplication;
import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.Metrica;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.AlreadyInGameException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Estado;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Visibilidad;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;

@SpringBootTest(classes = TrucoBeastsApplication.class)
@AutoConfigureTestDatabase
@Transactional
public class PartidaJugadorServiceTest {

    @Autowired
    private PartidaJugadorService pjService;

    @Autowired
    private PartidaService partidaService;

    
    public Partida partida;

    @BeforeEach
    public void setUp() {
        partida = partidaService.findPartidaById(0);
        List<PartidaJugadorDTO> jugadoresConectados = pjService.getPlayersConnectedTo(partida.getCodigo());
        jugadoresConectados.stream().filter(pj -> pj.getIsCreator() == false).forEach(pj -> pjService.eliminateJugadorPartida(pj.getPlayer().getId())); //Por que?

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
    @WithMockUser(username = "admin1", authorities = "ADMIN")
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

            res = pjService.getNumberOfGamesConnected(9);
            assertTrue(res.equals(1));
        } catch (AlreadyInGameException e) {
            fail("La excepción AlreadyInGameException no debería haberse lanzado.");
        }
    }

    @Test
    @WithMockUser(username = "player8", authorities = "PLAYER")
    public void eliminateJugadorPartida() {
        try {
            partida.setInstanteFin(null);
            partida.setInstanteInicio(null);
            partidaService.savePartida(partida);

            pjService.addJugadorPartida(partida, 10, false);

            Integer res = pjService.getNumJugadoresInPartida(0);
            assertTrue(res.equals(2));

            pjService.eliminateJugadorPartidaByJugadorId(10);
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
        try {
            pjService.addJugadorPartida(partida, 7, false);
        } catch (AlreadyInGameException e) {     
            fail("La excepción AlreadyInGameException no debería haberse lanzado.");;
        }
        

        pjService.eliminateJugadorPartidaByJugadorId(7);
        Integer res = pjService.getNumberOfGamesConnected(7);
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

    @Test
    public void getAllJugadoresPartida(){
        List<PartidaJugadorView> res= pjService.getAllJugadoresPartida("ABCDE");
        assertEquals(2, res.size());
    }

    @Test
    public void getAllJugadoresPartidaBadCodigo(){
        assertThrows(ResourceNotFoundException.class , () -> pjService.getAllJugadoresPartida("GCHDT"));
    }


 

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void eliminateJugadorPartidaNotAuthorized() {
        assertThrows(NotAuthorizedException.class, () -> {
            partida.setInstanteFin(null);
            partida.setInstanteInicio(null);
            partidaService.savePartida(partida);

            pjService.addJugadorPartida(partida, 10, false);
            pjService.eliminateJugadorPartida(10);
        });
    }



    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void eliminateJugadorPartidaResourceNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> {
            pjService.eliminateJugadorPartida(999);
        });
    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void actualizarPartidaJugador() throws AlreadyInGameException{ //Hecho por David
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);

        pjService.addJugadorPartida(partida, 2, false);

        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();
        assertEquals(Integer.valueOf(2), pj.getPlayer().getId());

        pj.setFloresCantadas(5);
        pjService.actualizarPartidaJugador(pj.getId(),pj);
        pj = pjService.getPartidaJugadorUsuarioActual();

        assertEquals(Integer.valueOf(2), pj.getPlayer().getId());
        assertEquals(Integer.valueOf(5), pj.getFloresCantadas());
    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void getPartidaJugadorUsuarioActual() throws AlreadyInGameException{ //Hecho por David
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);

        pjService.addJugadorPartida(partida, 2, false);

        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();
        assertEquals(Integer.valueOf(2), pj.getPlayer().getId());


    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void checkEndGame() throws AlreadyInGameException{ //Hecho por David
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);
        pjService.addJugadorPartida(partida, 2, false);
        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();

        pjService.checkEndGame(pj);
        Partida partidaAct=partidaService.findPartidaById(pj.getGame().getId());

        if(pj.getEquipo()==Equipo.EQUIPO1){
            assertEquals(partidaAct.getPuntosMaximos(), partidaAct.getPuntosEquipo2());
        }else{
            assertEquals(partidaAct.getPuntosMaximos(), partidaAct.getPuntosEquipo1());
        }
        assertEquals(Estado.FINISHED, partidaAct.getEstado());
        assertNotNull(partidaAct.getInstanteFin());

    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void sumar1Estadistica() throws AlreadyInGameException{ //Hecho por David
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);
        pjService.addJugadorPartida(partida, 2, false);
        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();
        Metrica metrica=Metrica.NUMERO_FLORES;
        assertEquals(Integer.valueOf(0), pj.getFloresCantadas());
        pjService.sumar1Estadistica(partida.getCodigo(),pj.getPosicion(),metrica);

        PartidaJugador pjAct = pjService.getPartidaJugadorUsuarioActual();
        assertEquals(Integer.valueOf(1), pjAct.getFloresCantadas());    
    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void obtenerPartidaJugadorSegunCodigoYPosicion() throws AlreadyInGameException{ //Hecho por David
        partida.setInstanteFin(null);
        partida.setInstanteInicio(null);
        partidaService.savePartida(partida);
        pjService.addJugadorPartida(partida, 2, false);
        PartidaJugador pj = pjService.getPartidaJugadorUsuarioActual();

        PartidaJugador pj2=pjService.obtenerPartidaJugadorSegunCodigoYPosicion(partida.getCodigo(),pj.getPosicion());
        assertEquals(pj.getId(), pj2.getId());
        assertEquals(pj.getIsCreator(), false);
        assertEquals(pj.getPosicion(), pj2.getPosicion());
    }

    @Test
    @WithMockUser(username = "player1", authorities = "PLAYER")
    public void getJugadoresInPartida() throws AlreadyInGameException{    //Hecho por David
        Partida partida2= new Partida();
        partida2.setCodigo("DAVID");
        partida2.setConFlor(true);
        partida2.setPuntosMaximos(30);
        partida2.setPuntosEquipo1(0);
        partida2.setPuntosEquipo2(0);
        partida2.setVisibilidad(Visibilidad.PUBLICA);
        partida2.setInstanteFin(null);
        partida2.setInstanteInicio(null);
        partida2.setNumJugadores(2);
        partidaService.savePartida(partida2);
        pjService.addJugadorPartida(partida2, 2, false);
        
        List<Integer> jugadores=pjService.getJugadoresInPartida(partida2.getCodigo());
        assertEquals(1, jugadores.size());
        assertEquals(Integer.valueOf(2), jugadores.get(0));

    }

}
