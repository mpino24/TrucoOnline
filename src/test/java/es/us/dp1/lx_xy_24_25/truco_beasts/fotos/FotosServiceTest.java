package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FotosServiceTest {

    @Mock
    private FotosRepository fotosRepository;

    @InjectMocks
    private FotosService fotosService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindNombresFotoByTipoNoFotos() {
       
        when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.OTRO)).thenReturn(List.of());

        List<String> result = fotosService.findNombresFotoByTipo(CategoriaFoto.OTRO);

        assertNotNull(result);
        assertEquals(0, result.size());  
    
    }
    @Test
    public void testFindNombresFotoByTipoConFotos() {
        List<String> fotosEsperadas = List.of("foto1.jpg", "foto2.jpg", "foto3.jpg");

        when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.TROFEO)).thenReturn(fotosEsperadas);

        List<String> result = fotosService.findNombresFotoByTipo(CategoriaFoto.TROFEO);

        assertNotNull(result);
        assertEquals(3, result.size());  // Debería devolver tres fotos
        assertEquals(fotosEsperadas, result);  // Debería devolver las fotos esperadas
    }
    @Test
public void testFindNombresFotoByTipoConCategoriaNull() {
    when(fotosRepository.findAllNombresByCategoria(null)).thenReturn(List.of());

    List<String> result = fotosService.findNombresFotoByTipo(null);

    assertNotNull(result);
    assertEquals(0, result.size());  // Debería devolver una lista vacía
}
@Test
public void testFindNombresFotoByTipoConUnaFoto() {
    List<String> fotosEsperadas = List.of("foto1.jpg");

    when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.PERFIL)).thenReturn(fotosEsperadas);

    List<String> result = fotosService.findNombresFotoByTipo(CategoriaFoto.PERFIL);

    assertNotNull(result);
    assertEquals(1, result.size());  // Debería devolver solo una foto
    assertEquals(fotosEsperadas, result);  // Debería devolver la foto esperada
}

@Test
public void testFindNombresFotoByTipoConVariasCategorias() {
    List<String> fotosCategoria1 = List.of("foto1.jpg", "foto2.jpg");
    List<String> fotosCategoria2 = List.of("foto3.jpg", "foto4.jpg");

    when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.TROFEO)).thenReturn(fotosCategoria1);
    when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.PERFIL)).thenReturn(fotosCategoria2);

    List<String> result1 = fotosService.findNombresFotoByTipo(CategoriaFoto.TROFEO);
    List<String> result2 = fotosService.findNombresFotoByTipo(CategoriaFoto.PERFIL);

    assertEquals(2, result1.size());  // Debería devolver dos fotos de la primera categoría
    assertEquals(2, result2.size());  // Debería devolver dos fotos de la segunda categoría
}
@Test
public void testFindNombresFotoByTipoConErrorEnRepositorio() {
    when(fotosRepository.findAllNombresByCategoria(CategoriaFoto.TROFEO))
            .thenThrow(new RuntimeException("Error en la base de datos"));

    try {
        fotosService.findNombresFotoByTipo(CategoriaFoto.TROFEO);
        fail("Se esperaba una excepción");
    } catch (RuntimeException e) {
        assertEquals("Error en la base de datos", e.getMessage());
    }
}


}