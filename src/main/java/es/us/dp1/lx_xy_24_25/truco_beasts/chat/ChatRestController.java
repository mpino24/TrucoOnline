package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "The Chats gestion API")
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatRestController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("/{chatId}")
    public List<Mensaje> getMensajesDe(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getMensajesDe(chatId);
    }

    @GetMapping("/lastMessage/{chatId}")
    public Mensaje getLastMessageFrom(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getLastMessage(chatId);
    }

    @GetMapping("/with/{userId}")
    public Chat getChatOf(@PathVariable Integer userId) {
        return chatService.findChatWith(userId);
    }

    @PostMapping("/with/{userId}")
    public Mensaje sendNewMessage(@PathVariable Integer userId, @RequestBody Mensaje mensaje) throws NotYourChatException {
        mensaje.setChat(chatService.findChatWith(userId));
        mensaje.setRemitente(userService.findCurrentUser());
        return chatService.guardarMensaje(mensaje);
    }
}
