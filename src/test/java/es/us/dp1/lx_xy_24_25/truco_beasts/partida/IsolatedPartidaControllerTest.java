package es.us.dp1.lx_xy_24_25.truco_beasts.partida;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(value = { PartidaController.class }, excludeFilters = @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, classes= WebSecurityConfigurer.class))
public class IsolatedPartidaControllerTest {

    @MockBean
    PartidaService ps;

    @Autowired
    MockMvc mvc;

    static final String BASE_URL="/api/v1/partida";
/* 
    @Test
    @WithMockUser(username= "admin", authorities="ADMIN")
    public void unfeasibleMatchCreationTrst(){
        Partida g= new Partida();
        ObjectMapper objectmapper = new ObjectMapper();

        reset(ps);
        mvc.perform(post(BASE_URL))
        .with(csrf())

    }*/

    
}
