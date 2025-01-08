package es.us.dp1.lx_xy_24_25.truco_beasts.exceptions;

public class PlayerNotConnectedException extends RuntimeException {


    public PlayerNotConnectedException(String username) {
        super("El jugador " + username + " no está conectado");
    }

    public PlayerNotConnectedException() {
        super("Hay jugadores que no están conectados");
    }


    
}
