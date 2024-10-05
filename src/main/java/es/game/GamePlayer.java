package es.game;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.your_game_name.player.Player;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GamePlayer extends BaseEntity{
    
    @NotNull
    Team equipo;
    
    @NotNull
    private Integer posicion;
    
    @OneToOne
    private Player player;

    @OneToOne
    private Game game;

}

