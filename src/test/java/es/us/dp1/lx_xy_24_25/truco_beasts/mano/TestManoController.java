package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;

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
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.carta.Carta;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.PartidaService;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Visibilidad;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;



@WebMvcTest(controllers = ManoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class TestManoController {

    private static final String BASE_URL = "/api/v1/manos/{codigo}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

    @MockBean
    private ManoService manoService;

    private User usuario0;
    private User usuario1;
    private Jugador jugador0;
    private Jugador jugador1;
    private Partida partida;
    private PartidaJugador jug0partida;
    private PartidaJugador jug1partida;

    @BeforeEach
    public void setup() {

        usuario0 = new User();
        usuario1 = new User();
        jugador0 = new Jugador();
        jugador1 = new Jugador();
        partida = new Partida();
        jug0partida = new PartidaJugador();
        jug1partida = new PartidaJugador();

        Authorities autoridadAdmin = new Authorities();
        autoridadAdmin.setId(1);
        autoridadAdmin.setAuthority("ADMIN");
        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(2);
        autoridadJugador.setAuthority("PLAYER");

        usuario0.setId(1);
        usuario0.setUsername("jugador0");
        usuario0.setPassword("jugador0");
        usuario0.setAuthority(autoridadJugador);
        jugador0.setId(1);
        jugador0.setUser(usuario0);

        usuario1.setId(2);
        usuario1.setUsername("jugador1");
        usuario1.setPassword("jugador1");
        usuario1.setAuthority(autoridadJugador);
        jugador1.setId(2);
        jugador1.setUser(usuario1);

        partida.setId(1);
        partida.setCodigo("TESTS");
        partida.setConFlor(true);
        partida.setNumJugadores(2);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
        partida.setPuntosMaximos(15);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        
        jug0partida.setId(1);
        jug0partida.setGame(partida);
        jug0partida.setPlayer(jugador0);
        jug0partida.setIsCreator(true);
        
        jug1partida.setId(2);
        jug1partida.setGame(partida);
        jug1partida.setPlayer(jugador1);
        jug1partida.setIsCreator(false);
    }

    public List<List<Carta>> cartas() {
        Carta c0 = new Carta();
        Carta c1 = new Carta();
        Carta c2 = new Carta();
        c0.setId(0);
        c1.setId(1);
        c2.setId(2);
        c0.setPoder(14);
        c1.setPoder(13);
        c2.setPoder(6);
        List<Carta> listaBase = new ArrayList<>();
        
        List<List<Carta>> cartasDisponibles = new ArrayList<>();
        listaBase.add(c0); listaBase.add(c1); listaBase.add(c2);
        cartasDisponibles.add(listaBase); cartasDisponibles.add(listaBase);
        return cartasDisponibles;
    }

    
	@Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void jugadorConTurnoDeberiaTirarCarta() throws Exception {
        List<List<Carta>> cartasDisponibles= cartas();
        Carta carta= cartasDisponibles.get(0).get(0);
        when(manoService.tirarCarta(partida.getCodigo(), carta.getId())).thenReturn(carta);

        mockMvc.perform(patch(BASE_URL+"/tirarCarta/{cartaId}", partida.getCodigo(), carta.getId())
                    .with(csrf()))
                    .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void deberiaActualizarManoYDevolverMano() throws Exception {
        Mano mano = new Mano();
        mano.setPartida(partida);
        when(manoService.getMano(partida.getCodigo())).thenReturn(mano);

        mockMvc.perform(get(BASE_URL, partida.getCodigo())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void jugadorDeberiaPoderCantarTruco() throws Exception {
        Cantos cantoTruco = Cantos.TRUCO;
        String codigoPartida = partida.getCodigo();
        Mano mano = new Mano();
        when(manoService.cantosTruco(codigoPartida, cantoTruco)).thenReturn(mano);

        mockMvc.perform(patch(BASE_URL + "/cantarTruco/{cantoTruco}", codigoPartida, cantoTruco)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void jugadorDeberiaPoderResponderTruco() throws Exception {
        Cantos respuestasTruco = Cantos.QUIERO;
        String codigoPartida = partida.getCodigo();
        doNothing().when(manoService).responderTruco(codigoPartida, respuestasTruco);
        Mano mano = new Mano();
        when(manoService.getMano(codigoPartida)).thenReturn(mano);

        mockMvc.perform(patch(BASE_URL + "/responderTruco/{respuestasTruco}", codigoPartida, respuestasTruco)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void jugadorDeberiaPoderCantarEnvido() throws Exception {
        Cantos cantoEnvido = Cantos.ENVIDO;
        String codigoPartida = partida.getCodigo();
        Mano mano = new Mano();
        when(manoService.cantosTruco(codigoPartida, cantoEnvido)).thenReturn(mano);
        
        mockMvc.perform(patch(BASE_URL + "/cantarEnvido/{cantoEnvido}", codigoPartida, cantoEnvido)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "player", authorities = {"PLAYER"})
    void jugadorDeberiaPoderResponderEnvido() throws Exception {
        Cantos cantoEnvido = Cantos.QUIERO;
        String codigoPartida = partida.getCodigo();
        doNothing().when(manoService).responderTruco(codigoPartida, cantoEnvido);
        Mano mano = new Mano();
        when(manoService.getMano(codigoPartida)).thenReturn(mano);

        mockMvc.perform(patch(BASE_URL + "/responderEnvido/{cantoEnvido}", codigoPartida, cantoEnvido)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
