package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;


@Component
public class PerfilSerializer extends JsonSerializer<PerfilJugadorUsuario> {

    @Override
    public void serialize(PerfilJugadorUsuario value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
       Jugador jug = value.getJugador();
       User us = value.getUser();
       gen.writeStartObject();
       gen.writeStringField("username", us.getUsername());
       gen.writeNumberField("userId", us.getId());
       gen.writeNumberField("jugadorId", jug.getId());
       gen.writeStringField("firstName", jug.getFirstName());
       gen.writeStringField("lastName", jug.getLastName());
       gen.writeStringField("email", jug.getEmail());
       gen.writeStringField("photo", jug.getPhoto());
       gen.writeEndObject();
       
    }
    

    
}