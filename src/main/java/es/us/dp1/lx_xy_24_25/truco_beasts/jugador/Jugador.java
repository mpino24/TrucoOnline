package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.Person;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugador;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Jugadores")
public class Jugador extends Person {

    @ManyToMany
    @JoinTable(
            name = "Amigo",
            joinColumns = @JoinColumn(name = "jugadorId"),
            inverseJoinColumns = @JoinColumn(name = "amigoId")
    )
    protected List<Jugador> amigos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "Solicitud",
            joinColumns = @JoinColumn(name = "solicitadoId"),
            inverseJoinColumns = @JoinColumn(name = "solicitanteId")
    )
    protected List<Jugador> solicitudes = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.ALL}, optional = false, orphanRemoval = true)
    @MapsId
    private User user;



}
