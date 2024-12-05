package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class NotYourChatException extends Exception {

    public NotYourChatException(){
        super("No perteneces al chat");
    }
    
    public NotYourChatException(String message){
        super(message);
    }
    
}
