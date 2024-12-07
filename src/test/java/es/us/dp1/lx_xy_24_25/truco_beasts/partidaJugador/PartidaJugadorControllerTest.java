package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorController;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import org.springframework.http.MediaType;

import org.springframework.context.annotation.FilterType;

@WebMvcTest(value = PartidaJugadorController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PartidaJugadorControllerTest {

    private static final String BASE_URL = "/api/v1/partidajugador";

    @MockBean
    private PartidaService partidaService;

    @MockBean
    private PartidaJugadorService partidaJugadorService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private PartidaJugadorView partidaJugadorView1;
    private PartidaJugadorView partidaJugadorView2;

    
    public void setupListaPartidaJugadorView(){
        partidaJugadorView1 = new PartidaJugadorView("player1", "hola", 1);
        partidaJugadorView2 = new PartidaJugadorView("player2", "adios", 2);

    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerListaPartidaJugadorView() throws Exception{
        setupListaPartidaJugadorView();
        List<PartidaJugadorView> listaJugadoresEnPartida = List.of(partidaJugadorView1, partidaJugadorView2);
        when(partidaJugadorService.getAllJugadoresPartida("ABCDE")).thenReturn(listaJugadoresEnPartida);

        mockMvc.perform(get(BASE_URL+ "//jugadores/codigoPartida/{codigo}", "ABCDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userName").value("player1"))
                .andExpect(jsonPath("$[0].foto").value("hola"))
                .andExpect(jsonPath("$[0].posicion").value(1))
                .andExpect(jsonPath("$[1].userName").value("player2"))
                .andExpect(jsonPath("$[1].foto").value("adios"))
                .andExpect(jsonPath("$[1].posicion").value(2));
         verify(partidaJugadorService).getAllJugadoresPartida("ABCDE");
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void errorObtencionListaJugadoresPartida() throws Exception{
        when(partidaJugadorService.getAllJugadoresPartida("ABCDE")).thenThrow(new ResourceNotFoundException("No se encontro"));
        mockMvc.perform(get(BASE_URL+ "//jugadores/codigoPartida/{codigo}", "ABCDE"))
            .andExpect(status().isNotFound());
            verify(partidaJugadorService).getAllJugadoresPartida("ABCDE");
    }



}
