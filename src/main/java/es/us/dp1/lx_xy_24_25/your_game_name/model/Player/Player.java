package es.us.dp1.lx_xy_24_25.your_game_name.model.Player;

import java.util.List;

import es.us.dp1.lx_xy_24_25.your_game_name.model.Person;
import es.us.dp1.lx_xy_24_25.your_game_name.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Player extends Person {

 

	@Column(name = "Amigos")
	@NotEmpty
	protected List<Player> Amigos;

    @OneToOne(cascade= {CascadeType.ALL})
    private User user;
    
    
}
 