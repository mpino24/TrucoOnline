package es.us.dp1.lx_xy_24_25.your_game_name.partida;


import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
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

    @Enumerated(EnumType.STRING)
    @NotNull
    Estado estado;

    @Column(name= "NUMJUGADORES")
    @NotEmpty
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
}
 