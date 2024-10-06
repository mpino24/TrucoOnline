package es.us.dp1.lx_xy_24_25.your_game_name.partida;

import es.us.dp1.lx_xy_24_25.your_game_name.jugador.Player;
import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PartidaJugador extends BaseEntity{
    
    @NotNull
    Equipo equipo;
    
    @NotNull
    private Integer posicion;
    
    @OneToOne
    private Player player;

    @OneToOne
    private Partida game;

}

