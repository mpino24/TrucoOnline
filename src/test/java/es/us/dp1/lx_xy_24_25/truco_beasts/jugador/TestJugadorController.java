package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = JugadorController.class)
public class TestJugadorController {
    @MockBean
    JugadorService jugadorService;

    @Autowired
    MockMvc mvc;

    static final String BASE_URL= "/api/v1/jugador";
    
   // public void testJugador() throws Exception{
        //reset(jugadorService);
        ///when(ts.ge)
    //}
}
