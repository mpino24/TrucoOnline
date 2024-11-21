package es.us.dp1.lx_xy_24_25.truco_beasts.partida;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;


@ExtendWith(MockitoExtension.class)
public class IsolatedPartidaServiceTest {
    
    @Autowired
    PartidaService ps;



    @BeforeEach
    public void setup(PartidaService partidaService){
        ps = partidaService; // Lo anterior era ps = new PartidaService(ps);
    }
    
}
