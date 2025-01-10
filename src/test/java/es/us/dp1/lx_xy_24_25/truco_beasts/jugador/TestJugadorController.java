package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@WebMvcTest(controllers = JugadorController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class TestJugadorController {

    private static final String BASE_URL = "/api/v1/jugador";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JugadorService jugadorService;

    @MockBean
    private UserService userService;


    private Jugador jugador;
    private JugadorDTO jugadorDTO;

    @BeforeEach
    public void setUp() {
        jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(1);

        jugador = new Jugador();
        jugador.setId(2);
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void findJugadorByUserId() throws Exception {
        when(jugadorService.findJugadorDTOByUserId(1)).thenReturn(jugadorDTO);

        mockMvc.perform(get(BASE_URL).param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deleteJugadorWithUserId() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{userId}", 1).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void findAmigosByUserId() throws Exception {
        List<JugadorDTO> amigos = Arrays.asList(new JugadorDTO(), new JugadorDTO());
        when(jugadorService.findAmigosByUserId(1)).thenReturn(amigos);

        mockMvc.perform(get(BASE_URL + "/amigos").param("userId", "1"))
                .andExpect(status().isOk());
    }

    
}
