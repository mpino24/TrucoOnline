package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FotosService {

    
    private final FotosRepository fotosRepository;
    @Autowired
    public FotosService(FotosRepository fotosRepository){
        this.fotosRepository=fotosRepository;
    }
    
    public List<String> findNombresFotoByTipo(CategoriaFoto categoriaFoto){
        return fotosRepository.findAllNombresByCategoria(categoriaFoto);
    }
}
