package es.us.dp1.lx_xy_24_25.your_game_name.partidajugador;

import es.us.dp1.lx_xy_24_25.your_game_name.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.your_game_name.partida.Equipo;
import es.us.dp1.lx_xy_24_25.your_game_name.partida.Partida;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PartidaJugador{
    
    @NotNull
    Equipo equipo;
    
    @NotNull
    private Integer posicion;
    
    @OneToOne
    private Jugador player;

    @OneToOne
    private Partida game;

}

