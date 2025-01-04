package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserRepository;

import jakarta.validation.Valid;

@Service
public class LogrosService {
  
    
    private final LogroRepository logroRepository;
    private final EstadisticasRepository estadisticasRepository;

    @Autowired
    public LogrosService(LogroRepository logroRepository, EstadisticasRepository estadisticasRepository){
        this.logroRepository= logroRepository;
        this.estadisticasRepository=estadisticasRepository;

    }


    @Transactional
    public Logros save(Logros logro) throws DataAccessException{
        logroRepository.save(logro);
        return logro;
    }

    @Transactional
    public Logros updateLogro(@Valid Logros logro){
        Optional<Logros> logroToUpdate = logroRepository.findById(logro.getId());
        if(logroToUpdate != null){
            BeanUtils.copyProperties(logro, logroToUpdate, "id");
        }
        return save(logro);
    }

    @Transactional(readOnly=true)
    public List<Logros> findAllLogros(){
        List<Logros> res= new ArrayList<>();
        logroRepository.findAll().forEach(x-> res.add(x));
        return res;
    }

    @Transactional(readOnly = true)
    public Logros findLogroByMetrica(Metrica metrica){
        return logroRepository.findByMetrica(metrica);
    }

    @Transactional(readOnly = true)
    public Logros findLogroById(Integer id){
        return logroRepository.findById(id).get();
    }

    @Transactional
    public void deleteLogro(Integer logroId){
        Logros logro = findLogroById(logroId);
        logroRepository.delete(logro);
    }

    public List<Logros> logrosConseguidos(Integer jugadorId){
        List<Logros> listaLogros= findAllLogros();
        List<Logros> misLogros= new ArrayList();
        for(Logros logro:listaLogros){
            Metrica metrica = logro.getMetrica();
            Integer miEstadistica= obtenerEstadisticaPorMetrica(metrica, jugadorId);
            if(miEstadistica>= logro.getValor()){
                misLogros.add(logro);
            }
        }
        return misLogros;
    }

    private Integer obtenerEstadisticaPorMetrica(Metrica metrica, Integer jugadorId) {
        String metodo = obtenerMetodoSegunMetrica(metrica);

        try {
            Method method = estadisticasRepository.getClass().getMethod(metodo, Integer.class);
            return (Integer) method.invoke(estadisticasRepository, jugadorId);
        } catch (Exception e) {
            e.printStackTrace();
            return null; 
        }
    }

    private String obtenerMetodoSegunMetrica(Metrica metrica) {
        switch (metrica) {
            case PARTIDAS_JUGADAS:
                return "findAllPartidasJugadas";
            case TIEMPO_JUGADO:
                return "findTiempoJugado";
            case VICTORIAS:
                return "findVictorias";
            case PARTIDAS_A_2: 
                return "findPartidasA2";
            case PARTIDAS_A_4:
                return "findPartidasA4";
            case PARTIDAS_A_6:
                return "findPartidasA6";
            case NUMERO_FLORES:
                return "findNumeroFlores";
            case NUMERO_ENGANOS:
                return "findNumeroEnganos";
            case QUIEROS:
                return "findQuieros";
            case NO_QUIEROS:
                return "findNoQuieros";
            case PARTIDAS_CON_FLOR:
                return "findPartidasConFlor";
            default:
                return null;
        }
    }
    
}






