package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class TrucoException extends RuntimeException {
    public TrucoException() {
        super("No podes cantar truco ni ninguna de sus variantes");
    }
    public TrucoException(String message) {
        super(message);
    }
}
