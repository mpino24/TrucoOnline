package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatosPorPartida {
    Boolean victorioso;

    LocalDateTime fecha;

    Boolean conFlor;

    Integer enganos;
    Integer atrapados;

    public DatosPorPartida(Boolean victorioso, LocalDateTime fecha, Boolean conFlor, Integer enganos, Integer atrapados) {
        this.victorioso = victorioso;
        this.fecha = fecha;
        this.conFlor = conFlor;
        this.enganos = (enganos != null) ? enganos : 0;
        this.atrapados = (atrapados != null) ? atrapados : 0;
    }
}
