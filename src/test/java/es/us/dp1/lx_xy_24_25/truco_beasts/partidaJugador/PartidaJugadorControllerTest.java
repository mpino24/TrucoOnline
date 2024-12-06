package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorController;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import org.springframework.context.annotation.FilterType;

import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PartidaJugadorController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PartidaJugadorControllerTest {

    private static final String BASE_URL = "/api/v1/partidajugador";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaJugadorService pjService;
    @MockBean
    private PartidaService partidaService;
    @MockBean
    private UserService userService;

    public List<PartidaJugadorView> setUp(){
        PartidaJugadorView jugador1 = new PartidaJugadorView("", "");
        PartidaJugadorView jugador2 = new PartidaJugadorView("", "");
        return List.of(jugador1, jugador2);
    }

    @Test
    @WithMockUser("PLAYER")
    public void testJugadoresPartida() throws Exception{
        List<PartidaJugadorView> valores= setUp();
        when(pjService.getAllJugadoresPartida("ABCDE")).thenReturn(valores); 

        mockMvc.perform(get(BASE_URL + "/jugadores/codigoPartida/{codigo}", "ABCDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        verify(pjService).getAllJugadoresPartida("ABCDE");

    }
    
}
