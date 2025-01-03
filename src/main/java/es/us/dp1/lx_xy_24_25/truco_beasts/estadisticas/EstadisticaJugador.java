package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import java.time.Duration;

import org.jpatterns.gof.CompositePattern.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class EstadisticaJugador {
    

    
    public Integer partidasJugadas = 0;
    public Integer tiempoJugado = 0; //Segundos 
    public Integer victorias = 0;
    public Integer derrotas = 0;
    public Integer partidasA2 = 0;
    public Integer partidasA4 = 0;
    public Integer partidasA6 = 0;
    public Integer numeroFlores = 0;
    public Integer numeroEnganos = 0;
    public Integer quieros = 0;
    public Integer noQuieros = 0;
    public Integer partidasConFlor=0;

}
