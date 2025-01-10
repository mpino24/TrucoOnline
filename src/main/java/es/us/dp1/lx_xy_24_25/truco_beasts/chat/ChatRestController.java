package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import es.us.dp1.lx_xy_24_25.truco_beasts.exceptions.NotYourChatException;
import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;
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
    public List<MensajeDTO> getMensajesDe(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getMensajesDe(chatId);
    }

    @GetMapping("/lastMessage/{chatId}")
    public MensajeDTO getLastMessageFrom(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getLastMessage(chatId);
    }

    @GetMapping("/users/{chatId}")
    public List<User> getUsersFromChat(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.findUsersByChat(chatId);
    }

    @GetMapping("/with/{userId}")
    public ChatDTO getChatOf(@PathVariable Integer userId) {
        return new ChatDTO(chatService.findChatWith(userId));
    }

    @PostMapping("/sendto/{userId}")
    public MensajeDTO sendNewMessage(@PathVariable Integer userId, @RequestBody Mensaje mensaje) throws NotYourChatException {
        mensaje.setChat(chatService.findChatWith(userId));
        mensaje.setRemitente(userService.findCurrentUser());
        return chatService.guardarMensaje(mensaje);
    }

    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity eliminarChat(@PathVariable Integer chatId){
        chatService.eliminarChat(chatId);
        return ResponseEntity.ok().build();
    }


    @PatchMapping("/{chatId}/updatetime")
    public ResponseEntity updateChatTime(@PathVariable Integer chatId){
        chatService.updateChatTime(chatId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread")
    public Integer getUnreadMessages(){
        return chatService.findNumAllNotReadMessages();
    }

}
