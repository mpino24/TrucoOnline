package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.truco_beasts.configuration.SecurityConfiguration;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.PerfilJugadorUsuario;

@WebMvcTest(controllers = CurrentUserRestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CurrentUserRestControllerTest {
    
    private final String BASE_URL = "/api/v1/profile";
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JugadorService jugadorService;

    @MockBean
    private UserService userService;

    private User usuario;
    private Jugador jugador;
    private JugadorDTO jugadorDTO;

    @BeforeEach
    public void setUp() {
        usuario = new User();
        usuario.setId(99);
        usuario.setUsername("prueba");
        usuario.setPassword("contraseña");

        jugador = new Jugador();
        jugador.setId(99);
        jugador.setFirstName("Pruebo");
        jugador.setLastName("Pruebin");
        jugador.setEmail("mail@mail.com");
        jugador.setPhoto("http://example.com/photo.jpg");
        jugador.setUser(usuario);

        jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(jugador.getId());
        jugadorDTO.setUserName(jugador.getUser().getUsername());
        jugadorDTO.setEmail(jugador.getEmail());
        jugadorDTO.setFirstName(jugador.getFirstName());
        jugadorDTO.setLastName(jugador.getLastName());
        jugadorDTO.setPhoto(jugador.getPhoto());

        when(userService.findCurrentUser()).thenReturn(usuario);
        doNothing().when(userService).updateConnection();
        when(jugadorService.findJugadorDTOByUserId(99)).thenReturn(jugadorDTO);
        when(jugadorService.findJugadorById(anyInt())).thenReturn(jugador);
    }

    @Test
    @WithMockUser(username = "prueba", authorities = "PLAYER")
    public void testGetProfile() throws Exception {
        mockMvc.perform(get(BASE_URL).principal(() -> "prueba"))  
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("prueba"))
                .andExpect(jsonPath("$.userId").value(99))
                .andExpect(jsonPath("$.jugadorId").value(99))
                .andExpect(jsonPath("$.firstName").value("Pruebo"))
                .andExpect(jsonPath("$.lastName").value("Pruebin"))
                .andExpect(jsonPath("$.email").value("mail@mail.com"))
                .andExpect(jsonPath("$.photo").value("http://example.com/photo.jpg"));
    }

    
}