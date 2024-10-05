package es.us.dp1.lx_xy_24_25.your_game_name.partida;


import es.us.dp1.lx_xy_24_25.your_game_name.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Partida extends BaseEntity{
    
    @NotNull
    Estado estado;

    @NotNull
    Tipo tipo;

    @Column(name = "conFlor")
	@NotEmpty
    private Boolean conFlor; 
    
    @Column(name = "PuntosMaximos")
	@NotEmpty
    Puntos puntosMaximos; // a 15 es false y a 30 true

    @Column(name = "PuntosE1")
	@NotEmpty
    @NotNull
    private Integer puntosEquipo1;

    @Column(name = "PuntosE2")
	@NotEmpty
    @NotNull
    private Integer puntosEquipo2;
}
