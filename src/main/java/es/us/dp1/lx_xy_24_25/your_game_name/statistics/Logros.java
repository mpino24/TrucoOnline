package es.us.dp1.lx_xy_24_25.your_game_name.statistics;

import es.us.dp1.lx_xy_24_25.your_game_name.model.NamedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Logros extends NamedEntity{
    @NotBlank
    private String descripcion;
    private String imagencita;

    @Min(0)
    private Integer valor;

    @Enumerated(EnumType.STRING)
    @NotNull
    Metrica metrica;

    public String getActualDescription(){
        return descripcion.replace("<VALOR>", String.valueOf(valor));
    }

}
