package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserDTO;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensajeDTO {

    @Column(name = "CONTENIDO")
    String contenido;

    @ManyToOne()
    @JoinColumn(name = "REMITENTE")
    UserDTO remitente;

    @Column(name = "FECHA")
    LocalDateTime fechaEnvio;

    public MensajeDTO() {

    }

    public MensajeDTO(Mensaje mensaje) {
        this.contenido = mensaje.getContenido();
        this.remitente = new UserDTO(mensaje.getRemitente());
        this.fechaEnvio = mensaje.getFechaEnvio();
    }

}
