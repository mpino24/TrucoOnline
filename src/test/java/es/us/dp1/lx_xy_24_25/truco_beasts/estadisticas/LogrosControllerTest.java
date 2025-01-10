package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;


import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@WebMvcTest(controllers = LogrosController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class LogrosControllerTest {


    private static final String BASE_URL = "/api/v1/logros";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
	private ObjectMapper objectMapper;

    @MockBean
    private LogrosService logrosService;

    @MockBean
    private UserService userService;

    @MockBean
    private JugadorService jugadorService;

    private User adminUser;
    private User normalUser;
    private Logros logro;

    private Jugador jugador;
    private JugadorDTO jugadorDTO;

    private Logros nuevoLogro;

    @BeforeEach
    public void setUp() {

        jugadorDTO = new JugadorDTO();
        jugadorDTO.setId(1);

        jugador= new Jugador();
        jugador.setId(2);



        Authorities autoridadPlayer = new Authorities();
        autoridadPlayer.setId(1);
        autoridadPlayer.setAuthority("PLAYER");

        Authorities autoridadAdmin = new Authorities();
        autoridadAdmin.setId(1);
        autoridadAdmin.setAuthority("ADMIN");

        adminUser = new User();
        adminUser.setId(1);
        adminUser.setUsername("admin");
        adminUser.setAuthority(autoridadAdmin);

        normalUser = new User();
        normalUser.setId(2);
        normalUser.setUsername("user");
        normalUser.setAuthority(autoridadPlayer);

        logro = new Logros();
        logro.setId(1);
        logro.setName("Primer Logro");
        logro.setDescripcion("Descripción del primer logro");

        nuevoLogro = new Logros();
        nuevoLogro.setId(2);
        nuevoLogro.setName("Nuevo Logro");
        nuevoLogro.setDescripcion("Este es un nuevo logro");
        nuevoLogro.setValor(10);
        nuevoLogro.setMetrica(Metrica.NO_QUIEROS);
        nuevoLogro.setOculto(false);
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void obtenerTodosLosLogros() throws Exception { when(userService.findCurrentUser()).thenReturn(adminUser); 
        when(jugadorService.findJugadorByUserId(adminUser.getId())).thenReturn(jugador); 
        when(logrosService.findAllLogros(anyBoolean(), anyInt())) .thenReturn(Arrays.asList(logro)); 
        
        mockMvc.perform(get(BASE_URL)) 
        .andExpect(status().isOk()) 
        .andExpect(jsonPath("$[0].name").value("Primer Logro")) 
        .andExpect(jsonPath("$[0].descripcion").value("Descripción del primer logro")); 
    }


    @Test
    void obtenerTodosLosLogrosSinAutorizacion() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getTotalLogros() throws Exception {
        when(logrosService.findTotalLogros()).thenReturn(10);

        mockMvc.perform(get(BASE_URL + "/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));
    }


    @Test
    void getTotalLogrosSinAutorizacion() throws Exception {
        mockMvc.perform(get(BASE_URL + "/total"))
                .andExpect(status().isUnauthorized());
    }

 
    @Test
    @WithMockUser(username = "player", roles = {"PLAYER"})
    void getMisLogros() throws Exception {
      

        when(userService.findCurrentUser()).thenReturn(normalUser); 
        when(jugadorService.findJugadorByUserId(normalUser.getId())).thenReturn(jugador); 
        when(logrosService.logrosConseguidos(anyInt())).thenReturn(Arrays.asList(logro)); 

    
 
        mockMvc.perform(get(BASE_URL+"/misLogros"))
                .andExpect(status().isOk()) 

                .andExpect(jsonPath("$[0].name").value("Primer Logro")) 
                .andExpect(jsonPath("$[0].descripcion").value("Descripción del primer logro")); 

    }
    

 
    @Test
    void getMisLogrosSinAutorizacion() throws Exception {
        mockMvc.perform(get(BASE_URL+"/misLogros"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createLogro() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(logrosService.save(logro)).thenReturn(logro);

        mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoLogro)))
                .andExpect(status().isCreated());
    }

   
    @Test
    void createLogroSinAutorizacion() throws Exception {
        mockMvc.perform(post(BASE_URL)
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(nuevoLogro)))
                .andExpect(status().isUnauthorized());
    }




    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteLogro() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(logrosService.findLogroById(1)).thenReturn(logro);

        mockMvc.perform(delete(BASE_URL+"/{logroId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
    }




    @Test
    void deleteLogroSinAutorizacion() throws Exception {
        when(userService.findCurrentUser()).thenReturn(normalUser);
        mockMvc.perform(delete(BASE_URL+"/{logroId}", 1)
                    .with(csrf()))
                .andExpect(status().isUnauthorized());
    }



    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateLogro() throws Exception {
        when(userService.findCurrentUser()).thenReturn(adminUser);
        when(logrosService.updateLogro(logro)).thenReturn(nuevoLogro);

        mockMvc.perform(put(BASE_URL)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoLogro)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateLogroSinAutorizacion() throws Exception {
        when(userService.findCurrentUser()).thenReturn(normalUser);
        mockMvc.perform(put("/api/v1/logros")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoLogro)))
                .andExpect(status().isUnauthorized());
    }
}
