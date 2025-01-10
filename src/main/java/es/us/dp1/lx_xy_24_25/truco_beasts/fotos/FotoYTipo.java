package es.us.dp1.lx_xy_24_25.truco_beasts.fotos;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "fotos")
public class FotoYTipo extends NamedEntity{
    
    @Enumerated(EnumType.STRING)
    @NotNull
    CategoriaFoto categoriaFoto;
}
