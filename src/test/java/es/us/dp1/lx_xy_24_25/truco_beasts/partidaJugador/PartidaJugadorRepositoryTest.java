package es.us.dp1.lx_xy_24_25.truco_beasts.partidaJugador;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorRepository;

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
        Optional<Partida> res = pjRepo.findPartidaByUserId(1);
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
    
}
