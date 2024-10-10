package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.CONFLICT)
@Getter
public class NameDuplicatedException extends RuntimeException {

	public NameDuplicatedException() {
		super("Access denied!");
	}
	
	public NameDuplicatedException(String message) {
		super(message);
	}
	
}
