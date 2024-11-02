package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter

public class EnvidoException extends Exception {
    public EnvidoException() {
        super("Solo pueden cantar envido el ultimo miembro de cada equipo");
    }
    public EnvidoException(String message) {
		super(message);
	}

    
}




