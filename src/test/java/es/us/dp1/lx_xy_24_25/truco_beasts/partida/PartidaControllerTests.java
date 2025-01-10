package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.PartidaNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import org.springframework.data.domain.Page;

@WebMvcTest(value = PartidaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PartidaControllerTests {

    private static final String BASE_URL = "/api/v1/partida";

    @Autowired
	private ObjectMapper objectMapper;

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
    private PartidaDTO partidaDTO1;
    private PartidaDTO partidaDTO2;

    private final Pageable pageable = PageRequest.of(0, 5, Sort.by(
            Order.asc("instanteInicio"),
            Order.desc("id")
    )); 

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

        partidaDTO1 = new PartidaDTO();
        partidaDTO2 = new PartidaDTO();

        partidaDTO1.setCodigo("TESTS");
        partidaDTO1.setVisibilidad("PRIVADA");
       
        partidaDTO2.setCodigo("TESTS2");
        partidaDTO2.setVisibilidad("PUBLICA");

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

    //hecho por Paula
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deberiaDevolverHistorialDePartidas() throws Exception {
        when(partidaService.findUsuarioDelJugadorActual()).thenReturn(usuarioJugador);
        when(partidaService.findHistorialDePartidas(any(), any(Pageable.class)))
                .thenReturn(new PageImpl<PartidaDTO>(Arrays.asList(partidaDTO1, partidaDTO2)));

        mockMvc.perform(get(BASE_URL + "/partidas/historial")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigo").value("TESTS"))
                .andExpect(jsonPath("$.content[1].codigo").value("TESTS2"));
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deberiaDevolverPartidasConParticipantes() throws Exception {

        List<PartidaDTO> partidasDTO = Arrays.asList(partidaDTO1); 
        Page<PartidaDTO> mockPage = new PageImpl<>(partidasDTO);
        when(partidaService.findPartidasYParticipantes(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get(BASE_URL + "/partidas/paginadas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].codigo").value("TESTS"));
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deberiaCrearPartida() throws Exception {

        Partida partidaNueva = new Partida();
        partidaNueva.setId(4);
        partidaNueva.setCodigo("Nuevo codigo");
        partidaNueva.setNumJugadores(2);
        partidaNueva.setConFlor(false);
        partidaNueva.setPuntosMaximos(15);
        partidaNueva.setVisibilidad(Visibilidad.PUBLICA);
        partidaNueva.setPuntosEquipo1(0);
        partidaNueva.setPuntosEquipo2(0);

        when(partJugService.getPartidaJugadorUsuarioActual()).thenReturn(null); 
        when(partidaService.savePartida(any())).thenReturn(partidaNueva);

        mockMvc.perform(post(BASE_URL)
                .param("userId", "2")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partidaNueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.codigo").value("Nuevo codigo"));
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void noDeberiaCrearPartidaSiYaEstaEnJuego() throws Exception {

        when(partJugService.getPartidaJugadorUsuarioActual()).thenReturn(new PartidaJugador()); 

        mockMvc.perform(post(BASE_URL)
                .param("userId", "2")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partidaA2)))
                .andExpect(status().isConflict());
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deberiaEliminarPartida() throws Exception {

        when(partidaService.findPartidaByCodigo("TESTS")).thenReturn(partidaA2);

        mockMvc.perform(delete(BASE_URL + "/TESTS")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void noDeberiaEliminarPartidaSiNoExiste() throws Exception {

        when(partidaService.findPartidaByCodigo("NOEXISTE")).thenReturn(null);

        mockMvc.perform(delete(BASE_URL + "/NOEXISTE")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void deberiaBuscarPartidaPorCodigo() throws Exception {

        when(partidaService.findPartidaByCodigo("TESTS")).thenReturn(partidaA2);

        mockMvc.perform(get(BASE_URL + "/search")
                .param("codigo", "TESTS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void noDeberiaEncontrarPartidaPorCodigoSiNoExiste() throws Exception {

        when(partidaService.findPartidaByCodigo("NOEXISTE")).thenReturn(null);

        mockMvc.perform(get(BASE_URL + "/search")
                .param("codigo", "NOEXISTE"))
                .andExpect(status().isNotFound());
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deberiaComenzarPartida() throws Exception {

        mockMvc.perform(patch(BASE_URL + "/TESTS/start")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Partida comenzada con éxito"));
    }

    //hecho por Paula
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void noDeberiaComenzarPartidaSiNoExiste() throws Exception {

        doThrow(new PartidaNotFoundException()).when(partidaService).startGame("NOEXISTE");

        mockMvc.perform(patch(BASE_URL + "/NOEXISTE/start")
                .with(csrf()))
                .andExpect(status().isNotFound());
    }










    
}
