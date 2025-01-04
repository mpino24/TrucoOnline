package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LogroRepository extends CrudRepository<Logros, Integer>{

    @Query("SELECT logro FROM Logros logro WHERE logro.metrica=:metrica")
    Logros findByMetrica(Metrica metrica);

    
}
