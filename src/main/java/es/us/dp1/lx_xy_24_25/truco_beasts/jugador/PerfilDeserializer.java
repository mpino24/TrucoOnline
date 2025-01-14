package es.us.dp1.lx_xy_24_25.truco_beasts.jugador;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@Component
public class PerfilDeserializer extends JsonDeserializer<PerfilJugadorUsuario>{
    
    @Autowired
    private UserService userService;

    @Autowired
    private JugadorService jugadorService;

    @Override
    public PerfilJugadorUsuario deserialize(JsonParser p, DeserializationContext ctx)
            throws IOException, JacksonException {
        PerfilJugadorUsuario resultado = new PerfilJugadorUsuario();
        Jugador jugador = new Jugador();
        User user = new User();
        JsonNode arbol = p.getCodec().readTree(p);
        String username = arbol.get("username").asText();
        Integer userId = arbol.get("userId").asInt();
        Integer jugadorId = arbol.get("jugadorId").asInt();
        String firstName = arbol.get("firstName").asText();
        String lastName = arbol.get("lastName").asText();
        String email = arbol.get("email").asText();
        String photo = arbol.get("photo").asText();
        String password = arbol.get("password")!=null ? arbol.get("password").asText() : null;
        User usuario = null; // Importante que este usuario no sea el mismo que el otro, ya que sino no veriamos los cambios
    try{

        jugadorService.existsJugador(jugadorId);
        userService.existsUser(username);
        
        usuario = userService.findUser(userId);

    } catch(Exception e){
        throw new IOException("Algo anduvo mal");
    }
    
    jugador.setFirstName(firstName);
    jugador.setLastName(lastName);
    jugador.setLastName(lastName);
    jugador.setEmail(email);
    jugador.setPhoto(photo);
    jugador.setUser(usuario);
 
    user.setUsername(username);
    user.setPassword(password);
    resultado.setJugador(jugador);
    resultado.setUser(user);
    
    return resultado;
        
    }
    
}
