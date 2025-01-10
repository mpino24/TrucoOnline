package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.Authorities;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserDTO;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;

@WebMvcTest(controllers = ChatRestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
public class ChatRestControllerTest {

    private static final String BASE_URL = "/api/v1/chat";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private UserService userService;

    private User usuario;
    private Chat chat;

    @BeforeEach
    public void setUpChat() {
        chat = new Chat();
        chat.setId(1);
        chat.setMensajes(List.of());
        List<ChatUsuario> chatUsuarios = new ArrayList<>();
        chat.setChatUsuarios(chatUsuarios);

    }

    @BeforeEach
    public void setUpUser() {
        usuario = new User();
        usuario.setId(1);
        usuario.setUsername("user1");
        usuario.setLastConnection(LocalDateTime.now());

        Authorities autoridadJugador = new Authorities();
        autoridadJugador.setId(1);
        autoridadJugador.setAuthority("PLAYER");

        usuario.setAuthority(autoridadJugador);

    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testGetMensajesDe() throws Exception { //Hecho por David
        Integer chatId = 1;
        List<MensajeDTO> mensajes = new ArrayList<>();
        Mensaje mensaje = new Mensaje();
        mensaje.setId(1);
        mensaje.setContenido("Hola");
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(usuario);
        mensajes.add(new MensajeDTO(mensaje));

        when(chatService.getMensajesDe(chatId)).thenReturn(mensajes);

        mockMvc.perform(get(BASE_URL + "/" + chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contenido").value("Hola"))
                .andExpect(jsonPath("$[0].fechaEnvio").exists())
                .andExpect(jsonPath("$[0].remitente").exists());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testGetLastMessageFrom() throws Exception { //Hecho por David
        Integer chatId = 1;
        MensajeDTO mensaje = new MensajeDTO();
        mensaje.setContenido("Hola");
        mensaje.setFechaEnvio(LocalDateTime.now());
        UserDTO usuarioDTO = new UserDTO(usuario);
        mensaje.setRemitente(usuarioDTO);

        when(chatService.getLastMessage(chatId)).thenReturn(mensaje);

        mockMvc.perform(get(BASE_URL + "/lastMessage/" + chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Hola"))
                .andExpect(jsonPath("$.fechaEnvio").exists())
                .andExpect(jsonPath("$.remitente").exists());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testGetUsersFromChat() throws Exception { //Hecho por David
        Integer chatId = 1;
        List<User> usuarios = new ArrayList<>();
        usuarios.add(usuario);

        when(chatService.findUsersByChat(chatId)).thenReturn(usuarios);

        mockMvc.perform(get(BASE_URL + "/users/" + chatId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[0].lastConnection").exists());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testGetChatOf() throws Exception { //Hecho por David
        Integer userId = 1;

        when(chatService.findChatWith(userId)).thenReturn(chat);

        mockMvc.perform(get(BASE_URL + "/with/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.usuarios").exists())
                .andExpect(jsonPath("$.mensajes").exists());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testSendNewMessage() throws Exception { //Hecho por David
        Integer userId = 1;
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido("Hola");
        mensaje.setFechaEnvio(LocalDateTime.now());
        mensaje.setRemitente(usuario);
        MensajeDTO mensajeDTO = new MensajeDTO(mensaje);

        when(chatService.findChatWith(userId)).thenReturn(chat);
        when(userService.findCurrentUser()).thenReturn(usuario);
        when(chatService.guardarMensaje(mensaje)).thenReturn(mensajeDTO);

        mockMvc.perform(post(BASE_URL + "/sendto/" + userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"contenido\":\"Hola\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Hola"))
                .andExpect(jsonPath("$.fechaEnvio").exists())
                .andExpect(jsonPath("$.remitente").exists());

    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testEliminarChat() throws Exception { //Hecho por David
        Integer chatId = 1;
        
        doNothing().when(chatService).eliminarChat(chatId);

        mockMvc.perform(delete(BASE_URL + "/" + chatId)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testUpdateChatTime() throws Exception { //Hecho por David
        Integer chatId = 1;

        doNothing().when(chatService).updateChatTime(chatId);

        mockMvc.perform(patch(BASE_URL + "/" + chatId + "/updatetime")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user1", roles = {"PLAYER"})
    public void testGetUnreadMessages() throws Exception { //Hecho por David
        Integer numMensajes = 1;

        when(chatService.findNumAllNotReadMessages()).thenReturn(numMensajes);

        mockMvc.perform(get(BASE_URL + "/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));

    }

}
