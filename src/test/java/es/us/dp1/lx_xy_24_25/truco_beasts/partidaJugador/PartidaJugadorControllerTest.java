package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

import org.springframework.http.MediaType;

import org.springframework.context.annotation.FilterType;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotAuthorizedException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.TeamIsFullException;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorController;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;

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

    @Autowired
	private ObjectMapper objectMapper;

    private PartidaJugadorView partidaJugadorView1;
    private PartidaJugadorView partidaJugadorView2;
    private PartidaJugador partidaJugador1;
    private PartidaJugador partidaJugador2;
    private PartidaJugador partidaJugador3;
    private User user1;
    private User user2;
    private User user3;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador jugador3;

    private Partida partida1;

    private PartidaJugadorDTO partidaJugadorDTO1;
    private PartidaJugadorDTO partidaJugadorDTO2;
    private PartidaJugadorDTO partidaJugadorDTO3;
    private PartidaJugadorDTO partidaJugadorDTO4;

    private JugadorDTO jugadorDTO1;
    private JugadorDTO jugadorDTO2;
    private JugadorDTO jugadorDTO3;
    private JugadorDTO jugadorDTO4;
    

    @BeforeEach
    public void setUpJugadorPartida(){
        user1 = new User();
        user2 = new User();
        user3 = new User();
        partida1 = new Partida();
        jugador1 = new Jugador();
        jugador2 = new Jugador();
        jugador3 = new Jugador();
        partidaJugador1= new PartidaJugador();
        partidaJugador2= new PartidaJugador();
        partidaJugador3= new PartidaJugador();


        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(2);
        autoridadJugador.setAuthority("PLAYER");


        user1.setId(1);
        user2.setId(2);
        user3.setId(3);
        user1.setUsername("player");
        user1.setAuthority(autoridadJugador);

        jugador1.setUser(user1);
        jugador1.setId(1);
        jugador2.setUser(user2);
        jugador2.setId(2);
        jugador3.setUser(user3);
        jugador3.setId(3);

        partida1.setCodigo("ABCDE");
        partida1.setId(1);


        partidaJugador1.setPosicion(3);
        partidaJugador1.setPlayer(jugador1);
        partidaJugador1.setGame(partida1);

        partidaJugador2.setPosicion(1);
        partidaJugador2.setPlayer(jugador2);
        partidaJugador2.setGame(partida1);

        partidaJugador3.setPosicion(2);
        partidaJugador3.setPlayer(jugador3);
        partidaJugador3.setGame(partida1);
   
    }
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerMiPosicion() throws Exception{

        when(userService.findCurrentUser()).thenReturn(user1);
        when(partidaJugadorService.getMiPosicion(user1.getId(), partida1.getId())).thenReturn(partidaJugador1.getPosicion());
        mockMvc.perform(get(BASE_URL+ "/miposicion/{partidaId}", partida1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
        verify(userService).findCurrentUser();
        verify(partidaJugadorService).getMiPosicion(user1.getId(), partida1.getId());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerMiPosicionExcepcionUser() throws Exception{
        when(userService.findCurrentUser()).thenThrow(new ResourceNotFoundException("No se encuentra el user"));
        mockMvc.perform(get(BASE_URL+ "/miposicion/{partidaId}", partida1.getId()))
                    .andExpect(status().isNotFound())
                    .andExpect(resultado -> resultado.getResponse().equals("No se encuentra el user"));;
        verify(userService).findCurrentUser();
        verify(partidaJugadorService, never()).getMiPosicion(user1.getId(), partida1.getId());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerMiPosicionExcepcionPartidaId() throws Exception{
        when(userService.findCurrentUser()).thenReturn(user1);
        when(partidaJugadorService.getMiPosicion(user1.getId(), partida1.getId())).thenThrow(new ResourceNotFoundException("No se encontro la partidaJugador"));
        mockMvc.perform(get(BASE_URL+ "/miposicion/{partidaId}", partida1.getId()))
                    .andExpect(status().isNotFound());
        verify(userService).findCurrentUser();
        verify(partidaJugadorService).getMiPosicion(user1.getId(), partida1.getId());
    }


    @Test 
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerNumJugadoresPartida() throws Exception{
        when(partidaJugadorService.getNumJugadoresInPartida(partida1.getId())).thenReturn(3);
        mockMvc.perform(get(BASE_URL+ "/numjugadores?partidaId=" + partida1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
        verify(partidaJugadorService).getNumJugadoresInPartida(partida1.getId());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerNumJugadoresPartidaSinParametro() throws Exception{
        
        mockMvc.perform(get(BASE_URL + "/numjugadores")
                        .with(csrf()))
                    .andExpect(status().isBadRequest());
                    
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void addJugadorPartida() throws Exception{

        when(userService.findCurrentUser()).thenReturn(user1);
        when(partidaService.findPartidaById(partida1.getId())).thenReturn(partida1);
        doNothing().when(partidaJugadorService).addJugadorPartida(partida1, user1.getId(), false);
        
        mockMvc.perform(post(BASE_URL + "/{partidaId}", partida1.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partida1)))
                .andExpect(status().isOk());
        
        verify(userService).findCurrentUser();
        verify(partidaService).findPartidaById(partida1.getId());
        verify(partidaJugadorService).addJugadorPartida(partida1, user1.getId(), false);
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void addJugadorPartidaExceptionUser() throws Exception{

        when(userService.findCurrentUser()).thenThrow(new ResourceNotFoundException("No se encuentra el user"));
        mockMvc.perform(post(BASE_URL + "/{partidaId}", partida1.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partida1)))
                .andExpect(status().isNotFound())
                .andExpect(resultado -> resultado.getResponse().equals("No se encuentra el user"));;
        verify(userService).findCurrentUser();
        verify(partidaService, never()).findPartidaById(partida1.getId());
        verify(partidaJugadorService, never()).addJugadorPartida(partida1, user1.getId(), false);
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerNumPartidasEnConexion() throws Exception{
        when(partidaJugadorService.getNumberOfGamesConnected(jugador1.getId())).thenReturn(1);
        mockMvc.perform(get(BASE_URL + "/connectedTo/{jugadorId}", jugador1.getId()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").value(1));
        verify(partidaJugadorService).getNumberOfGamesConnected(jugador1.getId());             
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void eliminarJugadorPartidaSinPasarJugador() throws Exception{
        doNothing().when(partidaJugadorService).eliminateJugadorPartidaByJugadorId(jugador1.getId());;
        mockMvc.perform(delete(BASE_URL + "/eliminarJugador")
                            .with(csrf()))
                        .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void eliminarJugadorPartidaPasandoJugador() throws Exception {
      doNothing().when(partidaJugadorService).eliminateJugadorPartidaByJugadorId(jugador2.getId());
      mockMvc.perform(delete(BASE_URL + "/eliminarJugador/{jugadorId}", jugador2.getId())
          .with(csrf()))
          .andExpect(status().isOk())
          .andExpect(content().string("Se elimino correctamente"));
      verify(partidaJugadorService).eliminateJugadorPartidaByJugadorId(jugador2.getId());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void eliminarJugadorPartidaExcepcionUser() throws Exception {
    doThrow(new NotAuthorizedException("No tienes permiso para eliminar a jugadores de la partida"))
        .when(partidaJugadorService).eliminateJugadorPartidaByJugadorId(jugador1.getId());
    mockMvc.perform(delete(BASE_URL + "/eliminarJugador/{jugadorId}", jugador1.getId())
        .with(csrf()))
        .andExpect(status().isForbidden())
        .andExpect(content().string("No tienes permiso para eliminar a jugadores de la partida"));
    verify(partidaJugadorService).eliminateJugadorPartidaByJugadorId(jugador1.getId());
    }


    public void setUpJugadoresPartidaDTO(){

        jugadorDTO1 = new JugadorDTO();
        jugadorDTO2 = new JugadorDTO();
        jugadorDTO3 = new JugadorDTO();
        jugadorDTO4 = new JugadorDTO();

        jugadorDTO1.setFirstName("Pedro");
        jugadorDTO2.setFirstName("Manolo");
        jugadorDTO3.setFirstName("Carlos");
        jugadorDTO4.setFirstName("Paco");

        partidaJugadorDTO1= new PartidaJugadorDTO();
        partidaJugadorDTO2= new PartidaJugadorDTO();
        partidaJugadorDTO3= new PartidaJugadorDTO();
        partidaJugadorDTO4= new PartidaJugadorDTO();

        partidaJugadorDTO1.setGame(partida1);
        partidaJugadorDTO1.setPlayer(jugadorDTO1);

        partidaJugadorDTO2.setGame(partida1);
        partidaJugadorDTO2.setPlayer(jugadorDTO2);

        partidaJugadorDTO3.setGame(partida1);
        partidaJugadorDTO3.setPlayer(jugadorDTO3);

        partidaJugadorDTO4.setGame(partida1);
        partidaJugadorDTO4.setPlayer(jugadorDTO4);

        


        
    }


    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerJugadoresConectadosPartida() throws Exception{
        setUpJugadoresPartidaDTO();
        when(partidaJugadorService.getPlayersConnectedTo(partida1.getCodigo())).thenReturn(List.of(partidaJugadorDTO1, partidaJugadorDTO2, partidaJugadorDTO3, partidaJugadorDTO4));
        mockMvc.perform(get(BASE_URL + "/players?partidaCode="+ partida1.getCodigo()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(4))
                    .andExpect(jsonPath("$[0].player.firstName").value(jugadorDTO1.getFirstName()))
                    .andExpect(jsonPath("$[1].player.firstName").value(jugadorDTO2.getFirstName()))
                    .andExpect(jsonPath("$[2].player.firstName").value(jugadorDTO3.getFirstName()))
                    .andExpect(jsonPath("$[3].player.firstName").value(jugadorDTO4.getFirstName()));

    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerJugadoresConectadosPartidaSinParametroCodigo() throws Exception{
        
        mockMvc.perform(get(BASE_URL + "/players")
                        .with(csrf()))
                    .andExpect(status().isBadRequest());
                    
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void cambiarEquipoDelJugador() throws Exception{
        doNothing().when(partidaJugadorService).changeTeamOfUser(user1.getId());
        mockMvc.perform(patch(BASE_URL + "/changeteam?userId="+user1.getId())
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void cambiarEquipoDelJugadorExcepcionUser() throws Exception{
        doThrow(new ResourceNotFoundException("El usuario no pertenece a la partida")).when(partidaJugadorService).changeTeamOfUser(user1.getId());
        mockMvc.perform(patch(BASE_URL + "/changeteam?userId="+user1.getId())
                        .with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(resultado -> resultado.getResponse().equals("El usuario no pertenece a la partida"));
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void cambiarEquipoDelJugadorExcepcionEquipoLleno() throws Exception{
        doThrow(new TeamIsFullException()).when(partidaJugadorService).changeTeamOfUser(user1.getId());
        mockMvc.perform(patch(BASE_URL + "/changeteam?userId="+user1.getId())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("El equipo ya está completo"));
    }


    
    public void setupListaPartidaJugadorView(){
        partidaJugadorView1 = new PartidaJugadorView("player1", "hola", 1);
        partidaJugadorView2 = new PartidaJugadorView("player2", "adios", 2);

    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void obtenerListaPartidaJugadorView() throws Exception{
        setupListaPartidaJugadorView();
        List<PartidaJugadorView> listaJugadoresEnPartida = List.of(partidaJugadorView1, partidaJugadorView2);
        when(partidaJugadorService.getAllJugadoresPartida(partida1.getCodigo())).thenReturn(listaJugadoresEnPartida);

        mockMvc.perform(get(BASE_URL+ "//jugadores/codigoPartida/{codigo}", partida1.getCodigo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userName").value("player1"))
                .andExpect(jsonPath("$[0].foto").value("hola"))
                .andExpect(jsonPath("$[0].posicion").value(1))
                .andExpect(jsonPath("$[1].userName").value("player2"))
                .andExpect(jsonPath("$[1].foto").value("adios"))
                .andExpect(jsonPath("$[1].posicion").value(2));
         verify(partidaJugadorService).getAllJugadoresPartida(partida1.getCodigo());
    }

    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void errorObtencionListaJugadoresPartida() throws Exception{
        when(partidaJugadorService.getAllJugadoresPartida(partida1.getCodigo())).thenThrow(new ResourceNotFoundException("No se encontro"));
        mockMvc.perform(get(BASE_URL+ "//jugadores/codigoPartida/{codigo}", partida1.getCodigo()))
            .andExpect(status().isNotFound());
            verify(partidaJugadorService).getAllJugadoresPartida(partida1.getCodigo());
    }



}
