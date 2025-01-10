package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FotosRepositoryTest {
    
     @Autowired
    FotosRepository fotosRepository;
  
 

    @Test
    public void pedirCategoriaQueNoExiste(){
        List<String> res =fotosRepository.findAllNombresByCategoria(CategoriaFoto.OTRO);
        
        assertEquals(List.of(), res);
    }
    @Test
    public void pedirCategoriaQueHay(){
        List<String> res =fotosRepository.findAllNombresByCategoria(CategoriaFoto.TROFEO);
        
        assertTrue(res.size() >0);
    }
    @Test
    public void pedirCategoriaNull() {
        List<String> res = fotosRepository.findAllNombresByCategoria(null);
        
        assertEquals(List.of(), res); 
    }

}
