package es.us.dp1.lx_xy_24_25.truco_beasts.partida;


import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Partida extends BaseEntity{


    @Column(name= "INICIO")
    LocalDateTime instanteInicio;

    @Column(name= "FIN")
    LocalDateTime instanteFin;

    @Column(name= "NUMJUGADORES")
    Integer numJugadores;

    @Column(name= "CODIGO")
    @NotEmpty
    private String codigo; 
    
    @Column(name = "CONFLOR")
	@NotNull
    private Boolean conFlor; 
    
    @Column(name = "PUNTOSMAXIMOS")
	@NotNull
    Integer puntosMaximos;

    @Column(name = "PUNTOSEQUIPO1")
    @NotNull
    private Integer puntosEquipo1;

    @Column(name = "PUNTOSEQUIPO2")
    private Integer puntosEquipo2;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Visibilidad visibilidad;

    private Integer jugadorMano;


    public Estado getEstado(){
        if(instanteInicio==null && instanteFin==null){
            return Estado.WAITING;
        }else if(instanteFin==null){
            return Estado.ACTIVE;
        }else{
            return Estado.FINISHED;
        }
    }


/*     public Partida clone(){
        Partida partida = new Partida();
        partida.setId(0);
        partida.setNumJugadores(2);
        partida.setCodigo("WWWWW");
        partida.setConFlor(false);
        partida.setPuntosMaximos(15);
        partida.setPuntosEquipo1(0);
        partida.setPuntosEquipo2(0);
        partida.setVisibilidad(Visibilidad.PUBLICA);
        partida.setInstanteInicio(LocalDateTime.of(2024, 11, 8, 20, 28));
        partida.setInstanteFin(LocalDateTime.of(2024, 11, 8, 20, 37));
        return partida;
    } */
}
 