package es.us.dp1.lx_xy_24_25.truco_beasts.carta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import es.us.dp1.lx_xy_24_25.truco_beasts.mano.Palo;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.context.annotation.ComponentScan;


@WebMvcTest(controllers = CartaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class CartaControllerTest {

    private static final String BASE_URL = "/api/v1/carta";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaService cartaService;

    @Test
    @WithMockUser(username = "user1", authorities = {"PLAYER"})
    public void findCartaById() throws Exception { // Hecho por David
        Integer cartaId = 1;
        Carta carta = new Carta();
        carta.setId(cartaId);
        carta.setPalo(Palo.OROS);
        carta.setValor(1);
        carta.setPoder(1);
        carta.setFoto("/foto.jpg");

        when(cartaService.findCartaById(cartaId)).thenReturn(carta);
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/id/" + cartaId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cartaId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.palo").value(Palo.OROS.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.valor").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.poder").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.foto").value("/foto.jpg"));

    }

}
