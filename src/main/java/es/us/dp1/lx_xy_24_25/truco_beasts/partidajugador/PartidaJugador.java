package es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador;

import es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas.Metrica;
import es.us.dp1.lx_xy_24_25.truco_beasts.jugador.Jugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Equipo;
import es.us.dp1.lx_xy_24_25.truco_beasts.partida.Partida;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class PartidaJugador extends BaseEntity{

    @NotNull
    private Integer posicion;
    
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Jugador player;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
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

    public Integer floresCantadas = 0;
    public Integer quierosCantados = 0;
    public Integer noQuierosCantados = 0;
    public Integer enganos = 0;
    
    @Transient
    public Gesto gesto;

    public void actualizarAtributoPorMetrica(Metrica metrica, Integer valor){
        switch (metrica) {
            case NUMERO_FLORES:
                setFloresCantadas(getFloresCantadas() + valor);
                break;
            case NUMERO_ENGANOS:
                setEnganos(getEnganos() + valor);
                break;
            case QUIEROS:
                setQuierosCantados(getQuierosCantados() + valor);
                break;
            case NO_QUIEROS:
                setNoQuierosCantados(getNoQuierosCantados() + valor);
                break;
            default:
                break;
        }
    }

   
    

}

