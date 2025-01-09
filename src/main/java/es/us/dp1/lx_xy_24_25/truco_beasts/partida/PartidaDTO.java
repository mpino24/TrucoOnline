package es.us.dp1.lx_xy_24_25.truco_beasts.partida;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class PartidaDTO {
    
    @NotNull
    private String codigo;
    @NotNull
    private String creador;
    @NotNull
    private String participantes;
    @NotNull
    private String tipo;
    @NotNull
    private String visibilidad;
    
    private String inicio = "-";

    private String fin = "-";

    public PartidaDTO() {
    }
    public PartidaDTO(String codigo, String creador, String participantes, String tipo, String visibilidad) {
        this.codigo = codigo;
        this.creador = creador;
        this.participantes = participantes;
        this.tipo = tipo;
        this.visibilidad = visibilidad;
    }
}
