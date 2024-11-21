package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;



@WebMvcTest(controllers = ManoController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class TestManoController {

    private static final String BASE_URL = "/api/v1/mano";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManoController manoController;

    
}
