package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = JugadorController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class JugadorControllerTests {

    private static final String BASE_URL = "/api/v1/jugador";
    @Autowired
	private ObjectMapper objectMapper;

    @MockBean
    private JugadorService jugadorService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private Jugador jugador;

    @BeforeEach
    public void setup() {
        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(1);
        autoridadJugador.setAuthority("PLAYER");

        user = new User();
        user.setId(1);
        user.setLastConnection(LocalDateTime.now());
        user.setUsername("user1");
        user.setAuthority(autoridadJugador);
        user.setPassword("contraseña");


        jugador = new Jugador();
        jugador.setUser(user);
        jugador.setId(1);
        jugador.setFirstName("jugador1");
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void deberiaDevolverJugadorByUserId() throws Exception {
        when(jugadorService.findJugadorDTOByUserId(1)).thenReturn(new JugadorDTO(jugador));
        mockMvc.perform(get(BASE_URL+"?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("user1"))
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void deberiaDevolverJugadorByUserIdEdit() throws Exception {
        when(jugadorService.findJugadorDTOByUserId(1)).thenReturn(new JugadorDTO(jugador));

        mockMvc.perform(get(BASE_URL+"/edit/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("user1"));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void deberiaDevolverAmigosPorUserId() throws Exception {
        List<JugadorDTO> amigos = Arrays.asList(new JugadorDTO(jugador));
        when(jugadorService.findAmigosByUserId(1)).thenReturn(amigos);

        mockMvc.perform(get(BASE_URL+"/amigos?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("user1"));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void noDeberiaDevolverAmigosSiNoExisten() throws Exception {
        when(jugadorService.findAmigosByUserId(1)).thenReturn(Arrays.asList());

        mockMvc.perform(get(BASE_URL+"/amigos?userId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void deberiaDevolverJugadorPorNombre() throws Exception {
        when(jugadorService.findJugadorByUserName("jugador1")).thenReturn(new JugadorDTO(jugador));

        mockMvc.perform(get(BASE_URL+"/jugador1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("user1"));
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void noDeberiaDevolverJugadorPorNombreSiNoExiste() throws Exception {
        when(jugadorService.findJugadorByUserName("inexistente")).thenReturn(null);

        mockMvc.perform(get(BASE_URL+"/inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin1", roles = {"ADMIN"})
    void deberiaEliminarJugador() throws Exception {
        doNothing().when(jugadorService).deleteJugadorByUserId(1);

        mockMvc.perform(delete(BASE_URL+"/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void noDeberiaEliminarJugadorSiNoExiste() throws Exception {
        doThrow(new ResourceNotFoundException("Jugador no encontrado")).when(jugadorService).deleteJugadorByUserId(999);

        mockMvc.perform(delete(BASE_URL+"/999").with(csrf()))
                .andExpect(status().isNotFound());
    }




    @Test
    @WithMockUser(username = "player1", roles = {"PLAYER"})
    void deberiaCrearSolicitud() throws Exception {
        doNothing().when(jugadorService).crearSolicitud(1, 2);

        mockMvc.perform(patch(BASE_URL+"/1/isSolicitado/2").with(csrf()))
                .andExpect(status().isOk());
    }



}
