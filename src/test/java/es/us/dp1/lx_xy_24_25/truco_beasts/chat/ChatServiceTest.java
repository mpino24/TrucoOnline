package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.ResourceNotFoundException;
import es.us.dp1.lx_xy_24_25.truco_beasts.partidajugador.PartidaJugadorService;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
public class ChatServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private UserService userService;

    @Mock
    private PartidaJugadorService partJugService;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatUsuarioRepository chatUsuarioRepository;

    @InjectMocks
    private ChatService chatService;

    private User testUser;
    private Chat testChat;
    private Mensaje testMensaje;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chatService = spy(chatService);

        testUser = new User();
        testUser.setId(1);
        testUser.setLastConnection(LocalDateTime.now().minusHours(2));

        testChat = new Chat();
        testChat.setId(1);

        testMensaje = new Mensaje();
        testMensaje.setId(1);
        testMensaje.setChat(testChat);
        testMensaje.setRemitente(testUser);

        when(userService.findUser(testUser.getId())).thenReturn(testUser);
        when(userService.findCurrentUser()).thenReturn(testUser);
    }

    @Test
    void testGuardarMensajeExito() throws NotYourChatException {
        when(chatRepository.findById(testChat.getId())).thenReturn(Optional.of(testChat));
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(testMensaje);

        MensajeDTO mensajeDTO = chatService.guardarMensaje(testMensaje);

        assertNotNull(mensajeDTO, "El mensaje DTO no debe ser nulo");
        verify(mensajeRepository).save(any(Mensaje.class));
    }

    @Test
    void testGuardarMensajeChatNoEncontrado() {
        when(chatRepository.findById(testChat.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NotYourChatException.class, () -> chatService.guardarMensaje(testMensaje));
        assertEquals("No puedes enviar mensajes a un chat que no existe", exception.getMessage());
    }

    @Test
    void testGetMensajesDeExito() throws NotYourChatException {
        doNothing().when(chatService).perteneceAChat(testChat.getId());

        when(chatRepository.findById(testChat.getId())).thenReturn(Optional.of(testChat));
        when(mensajeRepository.findMessagesFrom(testChat.getId())).thenReturn(List.of(testMensaje));

        List<MensajeDTO> mensajes = chatService.getMensajesDe(testChat.getId());

        assertNotNull(mensajes, "La lista de mensajes no debe ser nula");
        assertEquals(1, mensajes.size(), "Debe haber un mensaje en la lista");
        verify(mensajeRepository).findMessagesFrom(testChat.getId());
    }

    @Test
    void testGetLastMessageExito() {
        when(mensajeRepository.findLastMessage(testChat.getId())).thenReturn(Optional.of(testMensaje));

        MensajeDTO mensajeDTO = chatService.getLastMessage(testChat.getId());

        assertNotNull(mensajeDTO, "El último mensaje no debe ser nulo");
    }

    @Test
    void testEliminarChatExito() {
        doNothing().when(chatRepository).deleteById(testChat.getId());

        chatService.eliminarChat(testChat.getId());

        verify(chatRepository).deleteById(testChat.getId());
    }

    @Test
    void testUpdateChatTimeExito() {
        ChatUsuario chatUsuario = new ChatUsuario();
        chatUsuario.setChat(testChat);
        chatUsuario.setUser(testUser);
        chatUsuario.setFecha(LocalDateTime.now());

        when(chatUsuarioRepository.findChatUsuarioByUserAndChat(testUser.getId(), testChat.getId())).thenReturn(Optional.of(chatUsuario));

        chatService.updateChatTime(testChat.getId());

        verify(chatUsuarioRepository).save(any(ChatUsuario.class));
        assertNotNull(chatUsuario.getFecha(), "La fecha del chat debe actualizarse");
    }

    @Test
    void testUpdateChatTimeChatNoEncontrado() {
        when(chatUsuarioRepository.findChatUsuarioByUserAndChat(testUser.getId(), testChat.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> chatService.updateChatTime(testChat.getId()));
        assertEquals("No se ha encontrado el chatUsuario", exception.getMessage());
    }

    @Test
    void testCreateChatExito() {
        when(chatRepository.save(testChat)).thenReturn(testChat);

        chatService.createChat(testChat);

        verify(chatRepository).save(testChat);
    }

    @Test
    void testFindChatWithExito() {
        when(chatRepository.findChatBetween(2, testUser.getId())).thenReturn(testChat);

        Chat foundChat = chatService.findChatWith(2);

        assertNotNull(foundChat, "El chat encontrado no debe ser nulo");
        assertEquals(testChat, foundChat, "El chat encontrado debe coincidir con el esperado");
        verify(chatRepository).findChatBetween(2, testUser.getId());
    }

    @Test
    void testFindUsersByChatExito() {
        List<User> users = List.of(testUser);
        when(chatRepository.findUsersByChat(testChat.getId())).thenReturn(users);

        List<User> result = chatService.findUsersByChat(testChat.getId());

        assertNotNull(result, "La lista de usuarios no debe ser nula");
        assertEquals(1, result.size(), "Debe haber un usuario en la lista");
        assertEquals(testUser, result.get(0), "El usuario debe coincidir con el esperado");
        verify(chatRepository).findUsersByChat(testChat.getId());
    }

    @Test
    void testPerteneceAChatUserInChat() throws NotYourChatException {
        testChat.setUsuarios(List.of(testUser));
        when(chatRepository.findById(testChat.getId())).thenReturn(Optional.of(testChat));

        assertDoesNotThrow(() -> chatService.perteneceAChat(testChat.getId()));
    }

    @Test
    void testPerteneceAChatUserNotInChat() throws NotYourChatException {
        when(chatRepository.findById(testChat.getId())).thenReturn(Optional.of(testChat));
        when(userService.findCurrentUser()).thenReturn(testUser);
        User user1= new User();
        User user2= new User();
        List<User> usuarios= List.of(user1, user2);
        testChat.setUsuarios(usuarios);

        assertThrows(NotYourChatException.class, () -> chatService.perteneceAChat(testChat.getId()));
    }

    @Test
    void testFindNumNotReadMessagesExito() {
        LocalDateTime lastConnection = LocalDateTime.now().minusHours(1);
        ChatUsuario chatUsuario = new ChatUsuario();
        chatUsuario.setFecha(lastConnection);

        when(chatUsuarioRepository.findChatUsuarioByUserAndChat(testUser.getId(), testChat.getId())).thenReturn(Optional.of(chatUsuario));
        when(mensajeRepository.findMessagesAfter(testChat.getId(), lastConnection, testUser.getId())).thenReturn(5);

        int numNotReadMessages = chatService.findNumNotReadMessages(testChat.getId());

        assertEquals(5, numNotReadMessages, "El número de mensajes no leídos debe ser 5");
    }

    @Test
    void testFindNumAllNotReadMessagesExito() {
        ChatUsuario chatUsuario1 = new ChatUsuario();
        chatUsuario1.setChat(testChat);
        chatUsuario1.setFecha(LocalDateTime.now().minusHours(1));

        Chat testChat2 = new Chat();
        testChat2.setId(2);

        ChatUsuario chatUsuario2 = new ChatUsuario();
        chatUsuario2.setChat(testChat2);
        chatUsuario2.setFecha(LocalDateTime.now().minusHours(2));

        when(chatUsuarioRepository.findChatUsuarioByUser(testUser.getId())).thenReturn(List.of(chatUsuario1, chatUsuario2));
        when(chatUsuarioRepository.findChatUsuarioByUserAndChat(testUser.getId(), testChat.getId())).thenReturn(Optional.of(chatUsuario1));
        when(chatUsuarioRepository.findChatUsuarioByUserAndChat(testUser.getId(), testChat2.getId())).thenReturn(Optional.of(chatUsuario2));
        when(userService.findCurrentUser()).thenReturn(testUser);
        when(mensajeRepository.findMessagesAfter(testChat.getId(), chatUsuario1.getFecha(), testUser.getId())).thenReturn(3);
        when(mensajeRepository.findMessagesAfter(testChat2.getId(), chatUsuario2.getFecha(), testUser.getId())).thenReturn(2);

        int totalNotReadMessages = chatService.findNumAllNotReadMessages();

        assertEquals(5, totalNotReadMessages);
    }
}
