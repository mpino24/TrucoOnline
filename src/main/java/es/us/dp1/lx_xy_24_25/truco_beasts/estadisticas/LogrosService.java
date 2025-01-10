package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;

import jakarta.validation.Valid;

@Service
public class LogrosService {
  
    
    private final LogroRepository logroRepository;
    private final EstadisticasService estadisticasService;

    @Autowired
    public LogrosService(LogroRepository logroRepository, EstadisticasService estadisticasService){
        this.logroRepository= logroRepository;
        this.estadisticasService=estadisticasService;

    }


    @Transactional
    public Logros save(Logros logro) throws DataAccessException{
        return logroRepository.save(logro);
        
    }

    @Transactional
    public Logros updateLogro(@Valid Logros logro, Integer logroId){
        Logros logroToUpdate = logroRepository.findById(logroId).get();
        if(logroToUpdate != null){
            BeanUtils.copyProperties(logro, logroToUpdate, "id");
            return save(logroToUpdate);
        }else{
            throw new ResourceNotFoundException("El logro que querés actualizar con ID " + logroId + " no existe");
        }
        
    }

    @Transactional(readOnly=true)
    public List<Logros> findAllLogros(Boolean ocultosTambien, Integer jugadorId){
        List<Logros> res= new ArrayList<>();
        if(ocultosTambien || jugadorId==null){
            logroRepository.findAll().forEach(logro-> res.add(logro));
        }else{
            EstadisticaJugador estadisticaJugador = estadisticasService.getEstadisticasJugador(jugadorId);
            logroRepository.findAll().forEach(logro-> {
                if(!logro.getOculto() && !tieneLogro(estadisticaJugador.getEstadisticaPorMetrica(logro.getMetrica()), logro)){ //si no está oculto, o tiene el logro, lo muestra
                    res.add(logro);
                }
            });
        }
        
        

        return res;
    }

    @Transactional(readOnly = true)
    public List<Logros> findLogroByMetrica(Metrica metrica){
        return logroRepository.findByMetrica(metrica);
    }

    @Transactional(readOnly = true)
    public Logros findLogroById(Integer id) {
    Optional<Logros> logroOpt = logroRepository.findById(id);
    if (logroOpt.isEmpty()) {
        throw new ResourceNotFoundException("Logro no encontrado " + id);
    }
    return logroOpt.get();
}


    @Transactional
    public void deleteLogro(Integer logroId){
        Logros logro = findLogroById(logroId);
        logroRepository.delete(logro);
    }

    @Transactional
    public List<Logros> logrosConseguidos(Integer jugadorId){
        List<Logros> listaLogros= findAllLogros(true, null);
        List<Logros> misLogros= new ArrayList<>();
        EstadisticaJugador estadisticaGeneral = estadisticasService.getEstadisticasJugador(jugadorId);
        for(Logros logro:listaLogros){
            Metrica metrica = logro.getMetrica();
            Integer miEstadistica= estadisticaGeneral.getEstadisticaPorMetrica(metrica);
            if(tieneLogro(miEstadistica, logro)){
                misLogros.add(logro);
            }
        }
        return misLogros;
    }

    public Boolean tieneLogro(Integer estadistica,Logros logro){
        Boolean res = false;
        if(estadistica >= logro.getValor()){
            res = true;
        }
        return res;
    }


    public Integer findTotalLogros() {
        Integer res = (int) logroRepository.count();
        return res;
    }
   

    
    
}






