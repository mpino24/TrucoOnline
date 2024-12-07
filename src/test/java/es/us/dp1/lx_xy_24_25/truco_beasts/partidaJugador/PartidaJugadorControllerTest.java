package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import org.junit.Test;
import org.springframework.http.MediaType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorController;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
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

  
  @Test
  @WithMockUser("PLAYER")
  public void testGetMiPosicion() throws Exception{
    reset(pjService);
    reset(userService);
    User mockUser= mock(User.class);
    when(userService.findCurrentUser()).thenReturn(mockUser);
    when(userService.findCurrentUser().getId()).thenReturn(13);
    when(pjService.getMiPosicion(13, 1)).thenReturn(4);
    mockMvc.perform(get(BASE_URL + "/miposicion/{partidaId}", 1))
        .andExpect(status().isOk())        
        .andExpect(jsonPath("$").value(4));
    
    verify(userService, times(2)).findCurrentUser();
    verify(pjService).getMiPosicion(13, 1);
  }

  @Test
  @WithMockUser("PLAYER")
  public void testGetMiPosicionBadUser() throws Exception{
    reset(pjService);
    reset(userService);
    when(userService.findCurrentUser()).thenThrow(new ResourceNotFoundException("Nobody authenticated!") );
    mockMvc.perform(get(BASE_URL + "/miposicion/{partidaId}", 1))
            .andExpect(status().isNotFound());
    verify(userService).findCurrentUser();
    verify(pjService, never() ).getMiPosicion(13, 1);
  }


  @Test
  @WithMockUser("PLAYER")
  public void testGetMiPosicionBadPartidaJugador() throws Exception{
    reset(pjService);
    reset(userService);
    User mockUser= mock(User.class);
    when(userService.findCurrentUser()).thenReturn(mockUser);
    when(userService.findCurrentUser().getId()).thenReturn(13);
    when(pjService.getMiPosicion(13, 1)).thenThrow(new ResourceNotFoundException("No se encontro la partidaJugador pedida"));
    mockMvc.perform(get(BASE_URL + "/miposicion/{partidaId}", 1))
        .andExpect(status().isNotFound());

    verify(userService, times(2)).findCurrentUser();
    verify(pjService).getMiPosicion(13, 1);
  }



  @Test
  @WithMockUser("PLAYER")
  public void testGetNumJugadoresPartida() throws Exception{
    Partida partida = new Partida();
    partida.setId(1);

    when(pjService.getNumJugadoresInPartida(1)).thenReturn(4);
    mockMvc.perform(get(BASE_URL + "/numjugadores?partidaId=1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(4));
    verify(pjService).getNumJugadoresInPartida(1);
    
  }

  @Test
  @WithMockUser("PLAYER")
  public void testGetNumberOfGamesConnected() throws Exception{
    when(pjService.getNumberOfGamesConnected(1)).thenReturn(2);
    mockMvc.perform(get(BASE_URL + "/connectedTo/{jugadorId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(2));
    verify(pjService).getNumberOfGamesConnected(1);
    
  }



  List<PartidaJugadorDTO> setUpPartidaJugador(){
    PartidaJugadorDTO pj1 = new PartidaJugadorDTO();
    PartidaJugadorDTO pj2 = new PartidaJugadorDTO();
    return List.of(pj1,pj2);


  }

  @Test
  @WithMockUser("PLAYER")
  public void testGetPlayersConnectedTo() throws Exception{
    List<PartidaJugadorDTO> lista = setUpPartidaJugador();
    when(pjService.getPlayersConnectedTo("ABCDE")).thenReturn(lista);
    mockMvc.perform(get(BASE_URL + "/players?partidaCode=ABCDE"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
    verify(pjService).getPlayersConnectedTo("ABCDE");
    
  }







    List<PartidaJugadorView> setUp(){
        PartidaJugadorView jugador1 = new PartidaJugadorView("", "");
        PartidaJugadorView jugador2 = new PartidaJugadorView("", "");
        return List.of(jugador1, jugador2);
    }

    @Test
    @WithMockUser("PLAYER")
    public void testJugadoresPartida() throws Exception{
        reset(pjService);
        List<PartidaJugadorView> valores= setUp();
        when(pjService.getAllJugadoresPartida("ABCDE")).thenReturn(valores); 
        mockMvc.perform(get(BASE_URL + "/jugadores/codigoPartida/{codigo}", "ABCDE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        verify(pjService).getAllJugadoresPartida("ABCDE");

    }

    @Test 
    @WithMockUser("PLAYER") 
    public void testJugadoresPartidaBad() throws Exception{
        reset(pjService); 
        when(pjService.getAllJugadoresPartida("ABCDE")).thenThrow(new ResourceNotFoundException("No se encontro")); 
        mockMvc.perform(get(BASE_URL + "/jugadores/codigoPartida/{codigo}", "ABCDE")) 
            .andExpect(status().isNotFound()); 
        verify(pjService).getAllJugadoresPartida("ABCDE"); 
    }
    
}
