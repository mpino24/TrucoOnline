package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.JugadorDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartidaJugadorDTO {

    @NotNull
    private Integer posicion;

    @ManyToOne
    private JugadorDTO player;

    @ManyToOne
    private Partida game;

    @NotNull
    private Equipo equipo;

    @NotNull
    private Boolean isCreator;

    public PartidaJugadorDTO() {
    }

    public PartidaJugadorDTO(PartidaJugador pj) {
        this.posicion = pj.getPosicion();
        this.player = new JugadorDTO(pj.getPlayer());
        this.game = pj.getGame(); 
        this.equipo = pj.getEquipo();
        this.isCreator = pj.getIsCreator();
    }
}
