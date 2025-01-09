package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Getter;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Getter
public class PartidaNotFoundException extends RuntimeException {

    public PartidaNotFoundException() {
        super("La partida no existe!");

    }
    public PartidaNotFoundException(String message) {
		super(message);
	}
}
