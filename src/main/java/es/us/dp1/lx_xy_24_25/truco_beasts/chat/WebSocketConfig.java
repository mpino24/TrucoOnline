package es.us.dp1.lx_xy_24_25.truco_beasts.chat;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue"); // Habilita destinos para mensajes privados. Prefijo para enviar mensajes al cliente
        config.setApplicationDestinationPrefixes("/app"); // Prefijo para mensajes entrantes El cliente envia información al websocket. Prefijo destino
        //config.setUserDestinationPrefix("/user"); // Prefijo para mensajes específicos de usuario
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // Define el endpoint para WebSocket
                .setAllowedOrigins("*"); // Permite cualquier origen (útil en desarrollo, pero ajustar en producción)
                //.withSockJS(); // Habilita compatibilidad con SockJS
    }
}


