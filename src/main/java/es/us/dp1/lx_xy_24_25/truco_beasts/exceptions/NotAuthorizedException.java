package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Getter
public class NotAuthorizedException extends RuntimeException {
    	public NotAuthorizedException() {
		super("No tienes permiso para realizar dicha operación");
	}
	
	public NotAuthorizedException(String message) {
		super(message);
	}
    
}
