package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;


public class FotosControllerTest {

    @Mock
    private FotosService fotosService;

    private FotosController fotosController;
    private MockMvc mockMvc;
    private static final String BASE_URL = "/api/v1/fotos";
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fotosController = new FotosController(fotosService);
        mockMvc = MockMvcBuilders.standaloneSetup(fotosController).build();
    }

    @Test
    public void testFindAllTrofeosFotos() throws Exception {
        List<String> fotosEsperadas = List.of("foto1.jpg", "foto2.jpg", "foto3.jpg");

        when(fotosService.findNombresFotoByTipo(CategoriaFoto.TROFEO)).thenReturn(fotosEsperadas);

        mockMvc.perform(get(BASE_URL +"/trofeos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("foto1.jpg"))
                .andExpect(jsonPath("$[1]").value("foto2.jpg"))
                .andExpect(jsonPath("$[2]").value("foto3.jpg"));
    }
    @Test
public void testFindAllPerfilesFotos() throws Exception {
    List<String> fotosEsperadas = List.of("perfil1.jpg", "perfil2.jpg");

    when(fotosService.findNombresFotoByTipo(CategoriaFoto.PERFIL)).thenReturn(fotosEsperadas);

    mockMvc.perform(get(BASE_URL+"/perfiles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0]").value("perfil1.jpg"))
            .andExpect(jsonPath("$[1]").value("perfil2.jpg"));
}
@Test
public void testFindAllTrofeosFotosNoFotos() throws Exception {
    when(fotosService.findNombresFotoByTipo(CategoriaFoto.TROFEO)).thenReturn(List.of());

    mockMvc.perform(get(BASE_URL+"/trofeos"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));  
}
@Test
public void testFindAllPerfilesFotosNoFotos() throws Exception {
    when(fotosService.findNombresFotoByTipo(CategoriaFoto.PERFIL)).thenReturn(List.of());

    mockMvc.perform(get(BASE_URL +"/perfiles"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));  
}



}
