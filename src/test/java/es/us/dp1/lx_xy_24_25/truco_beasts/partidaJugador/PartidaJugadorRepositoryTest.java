package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;


import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorView;

@DataJpaTest() //Bedilia dice que no hacen falta tests del repositoyry excepto pruebas muy específicas
public class PartidaJugadorRepositoryTest {
    @Autowired
    PartidaJugadorRepository pjRepo;
    
    @Test
    public void findCreator(){
        PartidaJugador res = pjRepo.findCreator(0).get();
        assertTrue(res.getIsCreator());
    }

    @Test
    public void findCreatorBadGame(){
        Optional<PartidaJugador> res = pjRepo.findCreator(456);
        assertTrue(res.isEmpty());
    }

    @Test
    public void findPartidaByUserId(){
        Optional<Partida> res = pjRepo.findPartidaByUserId(9);
        assertTrue(res.get().getId().equals(0));
    }

    @Test
    public void findPartidaByUserIdBadUserId(){
        Optional<Partida> res = pjRepo.findPartidaByUserId(209);
        assertTrue(res.isEmpty());
    }

    @Test 
    public void findPlayersConnectedTo(){
        List<PartidaJugador> res= pjRepo.findPlayersConnectedTo("WWWWW");
        assertTrue(res.size()==1);
        assertTrue(res.get(0).getPlayer().getId()==9);
    }

    @Test 
    public void findPlayersConnectedToBadCode(){
        List<PartidaJugador> res= pjRepo.findPlayersConnectedTo("EDRFG");
        assertTrue(res.isEmpty());
    }

    @Test 
    public void lastPosition(){
        List<Integer> res= pjRepo.lastPosition(0);
        assertTrue(res.size()==1);
        assertTrue(res.get(0)==0);
    }

    @Test 
    public void lastPositionBadGame(){
        List<Integer> res= pjRepo.lastPosition(346);
        assertTrue(res.isEmpty());
    }

    @Test
    public void allJugadoresPartida(){
        List<PartidaJugadorView> res= pjRepo.findAllJugadoresPartida("ABCDE");
        assertEquals(2, res.size());
        assertEquals("http://localhost:8080/resources/images/jorge.jpg", res.get(0).getFoto());
    }
    
    @Test
    public void allJugadoresPartidaBad(){
        List<PartidaJugadorView> res= pjRepo.findAllJugadoresPartida("SDGER");
        assertTrue(res.isEmpty());
    }

    @Test 
    public void partidaJugadorByCodigo(){
        Optional<Partida> res= pjRepo.findPartidaByCodigoPartida("ABCDE");
        assertTrue(!res.isEmpty());
    } 

    @Test 
    public void partidaJugadorByCodigoBad(){
        Optional<Partida> res= pjRepo.findPartidaByCodigoPartida("SDGER");
        assertTrue(res.isEmpty());
    } 
}
