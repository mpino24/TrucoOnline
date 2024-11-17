package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
//Este hay que tocarlo basto
@Entity
@Getter
@Setter
public class PartidaJugador extends BaseEntity{

    @NotNull
    private Integer posicion;
    
    @ManyToOne
    private Jugador player;

    @ManyToOne
    private Partida game;

    @NotNull
    private Boolean isCreator;

    public Equipo getEquipo(){
        if(posicion%2==0){
            return Equipo.EQUIPO1;
        }else{
            return Equipo.EQUIPO2;
        }
    }

}

