package es.us.dp1.lx_xy_24_25.truco_beasts.user;

import java.time.LocalDateTime;

import es.us.dp1.lx_xy_24_25.truco_beasts.chat.ChatUsuario;
import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import jakarta.persistence.CascadeType;
import java.util.ArrayList;



@Getter
@Setter
@Entity
@Table(name = "appusers")
public class User extends BaseEntity {

    private static final long TIEMPO_CONEXION_SEGUNDOS = 30;

    @Column(unique = true)
    @NotNull
    String username;

    @NotNull
    String password;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "authority")
    Authorities authority;

    @NotNull
    LocalDateTime lastConnection;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatUsuario> chatJugadores = new ArrayList<>();

    public Boolean hasAuthority(String auth) {
        return authority.getAuthority().equals(auth);
    }

    public Boolean hasAnyAuthority(String... authorities) {
        Boolean cond = false;
        for (String auth : authorities) {
            if (auth.equals(authority.getAuthority())) {
                cond = true;
            }
        }
        return cond;
    }

    public Boolean isConnected() {
        return !this.lastConnection.isBefore(LocalDateTime.now().minusSeconds(TIEMPO_CONEXION_SEGUNDOS));
    }

    public User() {

    }

}
