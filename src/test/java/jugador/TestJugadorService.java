package jugador;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import es.us.dp1.lx_xy_24_25.truco_beasts.TrucoBeastsApplication;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;

@SpringBootTest(classes=TrucoBeastsApplication.class )
@AutoConfigureTestDatabase
public class TestJugadorService {

    @Autowired
    private JugadorService jugadorService;

    public TestJugadorService(){}

    @BeforeEach
    public void setup() {
        // Any setup code you need to run before each test
    }

    @Test
    void findAmigosByUserId(){
        Jugador jugador = jugadorService.findJugadorById(1);
        Jugador jugador2 = jugadorService.findJugadorById(2);
        
        jugadorService.addNewFriends(jugador.getUser().getId(), jugador2.getId());
        
        assertEquals(1, jugadorService.findAmigosByUserId(jugador.getUser().getId()).size());

        assertEquals(jugador2.getFirstName(), jugadorService.findAmigosByUserId(jugador.getUser().getId()).get(0).getFirstName());



        assertEquals(1, jugadorService.findAmigosByUserId(jugador2.getUser().getId()).size());

        assertEquals(jugador.getFirstName(), jugadorService.findAmigosByUserId(jugador2.getUser().getId()).get(0).getFirstName());


        




    }
}

