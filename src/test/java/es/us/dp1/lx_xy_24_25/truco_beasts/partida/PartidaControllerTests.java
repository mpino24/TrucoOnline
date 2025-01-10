package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@WebMvcTest(value = PartidaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PartidaControllerTests {

    private static final String BASE_URL = "/api/v1/partida";

    @MockBean
    private PartidaService partidaService;

    @MockBean
    private PartidaJugadorService partJugService;
    

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private User usuarioAdmin;
    private User usuarioJugador;
    private Partida partidaA2;
    private Partida partidaPrivada;
    private Partida partidaTerminada;

    private final Pageable pageable = PageRequest.of(0, 5, Sort.by(
            Order.asc("instanteInicio"),
            Order.desc("id")
    )); //BORRAR DESPUES SI NO SE USA

    @BeforeEach
    public void setup() {

        usuarioAdmin = new User();
        usuarioJugador = new User();

        Authorities autoridadAdmin = new Authorities();
        autoridadAdmin.setId(1);
        autoridadAdmin.setAuthority("ADMIN");

        usuarioAdmin.setId(1);
        usuarioAdmin.setLastConnection(LocalDateTime.now());
        usuarioAdmin.setUsername("admin");
        usuarioAdmin.setAuthority(autoridadAdmin);

        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(2);
        autoridadJugador.setAuthority("PLAYER");

        usuarioJugador.setId(2);
        usuarioJugador.setLastConnection(LocalDateTime.now());
        usuarioJugador.setUsername("player");
        usuarioJugador.setAuthority(autoridadJugador);

        partidaA2 = new Partida();
        partidaA2.setId(1);
        partidaA2.setCodigo("TESTS");
        partidaA2.setConFlor(true);
        partidaA2.setNumJugadores(2);
        partidaA2.setPuntosEquipo1(0);
        partidaA2.setPuntosEquipo2(0);
        partidaA2.setPuntosMaximos(15);
        partidaA2.setVisibilidad(Visibilidad.PUBLICA);

        partidaPrivada = new Partida();
        partidaPrivada.setId(2);
        partidaPrivada.setCodigo("TESTS");
        partidaPrivada.setConFlor(true);
        partidaPrivada.setNumJugadores(4);
        partidaPrivada.setPuntosEquipo1(0);
        partidaPrivada.setPuntosEquipo2(0);
        partidaPrivada.setPuntosMaximos(30);
        partidaPrivada.setVisibilidad(Visibilidad.PRIVADA);

        partidaTerminada = new Partida();
        partidaTerminada.setId(3);
        partidaTerminada.setCodigo("TESTS");
        partidaTerminada.setConFlor(true);
        partidaTerminada.setNumJugadores(4);
        partidaTerminada.setPuntosEquipo1(1);
        partidaTerminada.setPuntosEquipo2(30);
        partidaTerminada.setPuntosMaximos(30);
        partidaTerminada.setInstanteInicio(LocalDateTime.of(2024, 11, 9, 11, 30, 45));
        partidaTerminada.setInstanteFin(LocalDateTime.of(2024, 11, 9, 12, 0, 0));
        partidaTerminada.setVisibilidad(Visibilidad.PUBLICA);

        when(userService.findCurrentUser()).thenReturn(usuarioJugador);
        when(userService.findCurrentUser()).thenReturn(usuarioAdmin);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deberiaDevolverTodasLasPartidasAdmin() throws Exception {

        when(partidaService.findAllPartidas()).thenReturn(Arrays.asList(partidaA2, partidaPrivada, partidaTerminada));

        mockMvc.perform(get(BASE_URL + "/partidas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void noDeberiaDevolverPartidasPrivadasJugador() throws Exception {

        when(partidaService.findAllPartidas()).thenReturn(Arrays.asList(partidaA2));

        mockMvc.perform(get(BASE_URL + "/partidas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deberiaDevolverTodasLasPartidasPublicasJugador() throws Exception {

        List<Partida> partidas = Arrays.asList(partidaA2); 
        Page<Partida> mockPage = new PageImpl<>(partidas);
        when(partidaService.findAllPartidasActivas(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get(BASE_URL+"/partidas/accesibles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deberiaDevolverPartida() throws Exception {

        when(partidaService.findPartidaById(1)).thenReturn(partidaA2);

        mockMvc.perform(get(BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    
}
