package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class TeamIsFullException extends Exception {

        public TeamIsFullException() {
        super("El equipo ya está completo");

    }
    public TeamIsFullException(String message) {
		super(message);
	}
    
}
