package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.Person;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Jugadores")
public class Jugador extends Person{

	
	
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
        name = "Amigos",
        joinColumns = @JoinColumn(name = "jugador_id"),
        inverseJoinColumns = @JoinColumn(name = "amigo_id")
	)
	protected List<Jugador> amigos = new ArrayList<>();




    @OneToOne(cascade= {CascadeType.ALL}, optional = false)
    private User user;
    
    
}
 