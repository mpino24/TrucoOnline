package es.us.dp1.lx_xy_24_25.your_game_name.statistics;

import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Statistics extends BaseEntity {
    
    @NotNull
    private Integer tiempoJugado;

    @NotNull
    private Integer partidasJugadas;
    
    @NotNull
    private Integer victorias;
    
    @NotNull
    private Integer partidasA2;
    
    @NotNull
    private Integer partidasA4;
    
    @NotNull
    private Integer partidasA6;
    
    @NotNull
    private Integer numFlores;
    
    @NotNull
    private Integer numEnganos;
    
    @NotNull
    private Integer quieros;
    
    @NotNull
    private Integer noQuieros;

}
