package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

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
    public Integer atrapado = 0;

    public Integer getEstadisticaPorMetrica(Metrica metrica) {
        switch (metrica) {
            case PARTIDAS_JUGADAS:
                return partidasJugadas;
            case TIEMPO_JUGADO:
                return tiempoJugado;
            case VICTORIAS:
                return victorias;
            case DERROTAS:
                return derrotas;
            case PARTIDAS_A_2: 
                return partidasA2;
            case PARTIDAS_A_4:
                return partidasA4;
            case PARTIDAS_A_6:
                return partidasA6;
            case NUMERO_FLORES:
                return numeroFlores;
            case NUMERO_ENGANOS:
                return numeroEnganos;
            case ATRAPADO:
                return atrapado;
            case QUIEROS:
                return quieros;
            case NO_QUIEROS:
                return noQuieros;
            case PARTIDAS_CON_FLOR:
                return partidasConFlor;
            default:
                return null;
        }
    }
}
