package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.lx_xy_24_25.truco_beasts.model.BaseEntity;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Mensaje extends BaseEntity{
    
    @Column(name="CONTENIDO")
    String contenido;

    @ManyToOne()
    @JoinColumn(name = "REMITENTE")
    User remitente;

    @Column(name="FECHA")
    LocalDateTime fechaEnvio;

    @ManyToOne()
    @JsonIgnore
    Chat chat;

    public Mensaje(){
        
    }
    
}
