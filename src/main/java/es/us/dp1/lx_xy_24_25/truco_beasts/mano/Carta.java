package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Carta extends BaseEntity {
    
    private Palo palo;
    private Integer valor;
    private String foto;
}
