package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;

import org.springframework.context.annotation.FilterType;

@WebMvcTest(value = PartidaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class PartidaControllerTests {
    
    private static final String BASE_URL = "api/v1/partida";

    @Autowired
    private PartidaController partidaController;
    
    @Autowired
    private PartidaService partidaService;

    @Autowired
    private MockMvc mockMvc;

    private User usuarioAdmin;
    private User usuarioJugador;
    private Partida partidaA2;
    private Partida partidaPrivada;
    private Partida partidaTerminada;

    @BeforeEach
    public void setup() {

        Authorities autoridadAdmin = new Authorities();
        autoridadAdmin.setId(1);
        autoridadAdmin.setAuthority("ADMIN");

        usuarioAdmin.setId(1);
        usuarioAdmin.setUsername("admin");
        usuarioAdmin.setAuthority(autoridadAdmin);

        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(2);
        autoridadJugador.setAuthority("PLAYER");

        usuarioJugador.setId(2);
        usuarioJugador.setUsername("player");
        usuarioJugador.setAuthority(autoridadJugador);

        partidaA2.setId(1);
        partidaA2.setCodigo("TESTS");
        partidaA2.setConFlor(true);
        partidaA2.setNumJugadores(2);
        partidaA2.setPuntosEquipo1(0);
        partidaA2.setPuntosEquipo2(0);
        partidaA2.setPuntosMaximos(15);
        partidaA2.setVisibilidad(Visibilidad.PUBLICA);

        partidaPrivada.setId(2);
        partidaPrivada.setCodigo("TESTS");
        partidaPrivada.setConFlor(true);
        partidaPrivada.setNumJugadores(4);
        partidaPrivada.setPuntosEquipo1(0);
        partidaPrivada.setPuntosEquipo2(0);
        partidaPrivada.setPuntosMaximos(30);
        partidaPrivada.setVisibilidad(Visibilidad.PRIVADA);

        partidaTerminada.setId(3);
        partidaTerminada.setCodigo("TESTS");
        partidaTerminada.setConFlor(true);
        partidaTerminada.setNumJugadores(4);
        partidaTerminada.setPuntosEquipo1(1);
        partidaTerminada.setPuntosEquipo2(30);
        partidaTerminada.setPuntosMaximos(30);
        partidaTerminada.setInstanteInicio(LocalDateTime.of(2024, 11, 9, 11, 30, 45));
        partidaTerminada.setInstanteFin(LocalDateTime.of(2024, 11, 9, 12, 0, 0));
        partidaTerminada.setVisibilidad(Visibilidad.PUBLICA);
    }
}
