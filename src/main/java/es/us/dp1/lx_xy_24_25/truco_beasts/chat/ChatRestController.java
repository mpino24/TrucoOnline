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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/chat")
@Tag(name = "Chat", description = "La API de gestión de los Chats")
@SecurityRequirement(name = "bearerAuth")
public class ChatRestController {

    private final ChatService chatService;
    private final UserService userService;

    public ChatRestController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @Operation(summary = "Obtener mensajes de un chat específico")
    @GetMapping("/{chatId}")
    public List<MensajeDTO> getMensajesDe(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getMensajesDe(chatId);
    }

    @Operation(summary = "Obtener el último mensaje de un chat específico")
    @GetMapping("/lastMessage/{chatId}")
    public MensajeDTO getLastMessageFrom(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.getLastMessage(chatId);
    }

    @Operation(summary = "Obtener usuarios de un chat específico")
    @GetMapping("/users/{chatId}")
    public List<User> getUsersFromChat(@PathVariable Integer chatId) throws NotYourChatException {
        return chatService.findUsersByChat(chatId);
    }

    @Operation(summary = "Obtener chat con un usuario específico")
    @GetMapping("/with/{userId}")
    public ChatDTO getChatOf(@PathVariable Integer userId) {
        return new ChatDTO(chatService.findChatWith(userId));
    }

    @Operation(summary = "Enviar un nuevo mensaje a un usuario específico")
    @PostMapping("/sendto/{userId}")
    public MensajeDTO sendNewMessage(@PathVariable Integer userId, @RequestBody Mensaje mensaje) throws NotYourChatException {
        mensaje.setChat(chatService.findChatWith(userId));
        mensaje.setRemitente(userService.findCurrentUser());
        return chatService.guardarMensaje(mensaje);
    }

    @Operation(summary = "Eliminar un chat específico")
    @DeleteMapping("/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity eliminarChat(@PathVariable Integer chatId){
        chatService.eliminarChat(chatId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizar el tiempo de un chat específico")
    @PatchMapping("/{chatId}/updatetime")
    public ResponseEntity updateChatTime(@PathVariable Integer chatId){
        chatService.updateChatTime(chatId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Obtener el número de mensajes no leídos")
    @GetMapping("/unread")
    public Integer getUnreadMessages(){
        return chatService.findNumAllNotReadMessages();
    }

}
