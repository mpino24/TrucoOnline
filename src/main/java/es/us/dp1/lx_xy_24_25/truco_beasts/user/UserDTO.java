package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {

    Integer id;

    String username;

    Authorities authority;
    
    LocalDateTime lastConnection;

    Boolean isConnected;

    public UserDTO(){}
    
    public UserDTO(User u){
        this.id=u.getId();
        this.username=u.getUsername();
        this.authority=u.getAuthority();
        this.lastConnection=u.getLastConnection();
        this.isConnected = u.isConnected();
    }


    
}
