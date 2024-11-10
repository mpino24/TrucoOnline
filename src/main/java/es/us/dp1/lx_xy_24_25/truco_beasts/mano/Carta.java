package es.us.dp1.lx_xy_24_25.truco_beasts.mano;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="Cartas")
public class Carta extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Palo palo;

    @NotNull
    private Integer valor;

    @NotNull
    private Integer poder;

    private String foto;
}
