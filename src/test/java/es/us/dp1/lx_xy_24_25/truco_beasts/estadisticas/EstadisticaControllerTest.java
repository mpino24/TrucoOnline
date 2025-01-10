package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;


import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@WebMvcTest(controllers = EstadisticasController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class EstadisticaControllerTest {
    private static final String BASE_URL = "/api/v1/estadisticas";


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadisticasService estadisticasService;

    @MockBean
    private JugadorService jugadorService;

    @MockBean
    private UserService userService;

    private User user1;
    private Jugador jugador1;
    private EstadisticaJugador estadisticasJugador1;
    private EstadisticaGlobal estadisticaGlobal1;

    @BeforeEach
    public void setUpEstadistica(){
        user1 = new User();

        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(1);
        autoridadJugador.setAuthority("PLAYER");

        user1.setAuthority(autoridadJugador);
        user1.setId(1);
        user1.setLastConnection(LocalDateTime.now());
        user1.setUsername("user1");

        jugador1 = new Jugador();
        jugador1.setId(1);
        estadisticasJugador1 = new EstadisticaJugador();

        estadisticasJugador1.setDerrotas(2);
        estadisticasJugador1.setNoQuieros(5);
        estadisticasJugador1.setNumeroFlores(6);
        estadisticasJugador1.setTiempoJugado(1900);

        estadisticaGlobal1 = new EstadisticaGlobal();
        estadisticaGlobal1.setDerrotas(2);
        estadisticaGlobal1.setNoQuieros(5);
        estadisticaGlobal1.setNumeroFlores(6);
        estadisticaGlobal1.setTiempoJugado(1900);
        estadisticaGlobal1.setJugadoresTotales(4);
        estadisticaGlobal1.setPartidasConFlor(2);
    }

    @BeforeEach
    public void setUpEstadisticaGlobal(){
        estadisticaGlobal1 = new EstadisticaGlobal();
        estadisticaGlobal1.setDerrotas(2);
        estadisticaGlobal1.setNoQuieros(5);
        estadisticaGlobal1.setNumeroFlores(6);
        estadisticaGlobal1.setTiempoJugado(1900);
        estadisticaGlobal1.setJugadoresTotales(4);
        estadisticaGlobal1.setPartidasConFlor(2);
    }


    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerMisEstadisticas() throws Exception{

        when(userService.findCurrentUser()).thenReturn(user1);
        when(jugadorService.findJugadorByUserId(user1.getId())).thenReturn(jugador1);
        when(estadisticasService.getEstadisticasJugador(jugador1.getId())).thenReturn(estadisticasJugador1);
        mockMvc.perform(get(BASE_URL + "/misEstadisticas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.derrotas").value(2))
                .andExpect(jsonPath("$.noQuieros").value(5))
                .andExpect(jsonPath("$.numeroFlores").value(6))
                .andExpect(jsonPath("$.tiempoJugado").value(1900));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerEstadisticasGlobales() throws Exception{

        when(estadisticasService.getEstadisticasGlobales()).thenReturn(estadisticaGlobal1);
        mockMvc.perform(get(BASE_URL + "/estadisticasGlobales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.derrotas").value(2))
                .andExpect(jsonPath("$.noQuieros").value(5))
                .andExpect(jsonPath("$.numeroFlores").value(6))
                .andExpect(jsonPath("$.tiempoJugado").value(1900))
                .andExpect(jsonPath("$.partidasConFlor").value(2))
                .andExpect(jsonPath("$.jugadoresTotales").value(4));

    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerEstadisticasJugadorId() throws Exception{
        when(estadisticasService.getEstadisticasJugador(jugador1.getId())).thenReturn(estadisticasJugador1);
        mockMvc.perform(get(BASE_URL + "/estadisticasJugador/{jugadorId}", jugador1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.derrotas").value(2))
                .andExpect(jsonPath("$.noQuieros").value(5))
                .andExpect(jsonPath("$.numeroFlores").value(6))
                .andExpect(jsonPath("$.tiempoJugado").value(1900));

    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerEstadisticasJugadorIdNoEncontrado() throws Exception{
        doThrow(new ResourceNotFoundException("No se encontro")).when(estadisticasService).getEstadisticasJugador(45);
        mockMvc.perform(get(BASE_URL + "/estadisticasJugador/{jugadorId}", 45))
                .andExpect(status().isNotFound())
               ;

    }

    
}
