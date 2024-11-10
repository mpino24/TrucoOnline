package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lx_xy_24_25.truco_beasts.TrucoBeastsApplication;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;

@SpringBootTest(classes=TrucoBeastsApplication.class )
@AutoConfigureTestDatabase
public class TestJugadorService {

    @Autowired
    private JugadorService jugadorService;

    public TestJugadorService(){}
    Jugador jugador;
    Jugador jugador2;

    @BeforeEach
    public void setup() {
         jugador = jugadorService.findJugadorById(1);
         jugador2 = jugadorService.findJugadorById(2);
    }

    @Test
    void testFindAmigosByUserId(){
        
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());
        
        assertEquals(1, jugadorService.findAmigosByUserId(jugador.getUser().getId()).size());

        assertEquals(jugador2.getFirstName(), jugadorService.findAmigosByUserId(jugador.getUser().getId()).get(0).getFirstName());



        assertEquals(1, jugadorService.findAmigosByUserId(jugador2.getUser().getId()).size());

        assertEquals(jugador.getFirstName(), jugadorService.findAmigosByUserId(jugador2.getUser().getId()).get(0).getFirstName());
    }
    
    @Test
    void testCheckIfAreFriends(){
        
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());

        assertTrue(jugadorService.checkIfAreFriends(jugador.getUser().getUsername(), jugador2.getUser().getId()));
    }
    
    @Test
    void TestfindJugadorByUserName(){
        assertEquals(jugador.getFirstName(), jugadorService.findJugadorByUserName(jugador.getUser().getUsername()).getFirstName());
    }


        @Test
    void testAddFriendAgainFallo(){
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());

        assertThrows(IllegalStateException.class,() -> jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId()));
    }

    @Test
    void testAddYourselfFallo(){

        assertThrows(IllegalStateException.class,() -> jugadorService.addNewFriends(jugador.getUser().getId(), jugador.getId()));
    }

        @Test
    void testAddNotFoundFriendFallo(){
        assertThrows(ResourceNotFoundException.class,() -> jugadorService.addNewFriends(jugador.getUser().getId(),50));
    }

    @Test
    void TestFindJugadorByUserName(){
        assertEquals(jugador.getFirstName(), jugadorService.findJugadorByUserName(jugador.getUser().getUsername()).getFirstName());
    }

    @Test
    void TestFindNotFoundJugadorByUserNameFallo(){
        assertThrows(ResourceNotFoundException.class,() -> jugadorService.findJugadorByUserName("Manolito074k"));
    }
}

