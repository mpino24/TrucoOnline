package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FotosRepository extends CrudRepository<FotoYTipo, Long>{

    @Query("SELECT f.name FROM FotoYTipo f WHERE f.categoriaFoto = :categoriaFoto")
    List<String> findAllNombresByCategoria(CategoriaFoto categoriaFoto);
    
} 
