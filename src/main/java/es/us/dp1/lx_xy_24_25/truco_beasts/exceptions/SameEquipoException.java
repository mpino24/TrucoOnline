
package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class SameEquipoException extends Exception {
    public SameEquipoException() {
        super("Jugadores del mismo equipo no pueden aceptar/rechazar un truco");

    }
    public SameEquipoException(String message) {
		super(message);
	}
}

