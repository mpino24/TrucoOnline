package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "The Chats gestion API")
public class ChatRestController {

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatId}")
    public List<Mensaje> getMensajesDe(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getMensajesDe(chatId);
    }

    @GetMapping("/lastMessage/{chatId}")
    public Mensaje getLastMessageFrom(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getLastMessage(chatId);
    }

    @GetMapping("/users/{chatId}")
    public List<User> getUsersFromChat(@PathVariable Integer chatId) throws NotYourChatException {
        return null;
    }

    @GetMapping("/with/{userId}")
    public Chat getChatOf(@PathVariable Integer userId) {
        return chatService.findChatWith(userId);
    }
}