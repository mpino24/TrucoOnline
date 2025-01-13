package es.us.dp1.lx_xy_24_25.truco_beasts.estadisticas;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.NamedEntity;
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
    @NotBlank(message = "No puede estar vacia la descripción")
    private String descripcion;

    private String imagencita = "http://localhost:8080/resources/images/trofeos/trofeo1.jpg";

    @Min(0)
    private Integer valor;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "La metrica no puede ser nula")
    Metrica metrica;


    @NotNull(message = "Debe estar oculto o no, pero no null")
    Boolean oculto = false;

    public String getActualDescription(){
        return descripcion.replace("<VALOR>", String.valueOf(valor));
    }

}
