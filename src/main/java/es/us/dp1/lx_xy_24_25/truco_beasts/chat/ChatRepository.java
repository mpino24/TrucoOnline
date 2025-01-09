package es.us.dp1.lx_xy_24_25.truco_beasts.chat;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.us.dp1.lx_xy_24_25.truco_beasts.user.User;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer>{
    
    @Query("SELECT cu.chat FROM ChatUsuario cu WHERE cu.user.id = :amigoId AND cu.chat.id IN (SELECT cu2.chat.id FROM ChatUsuario cu2 WHERE cu2.user.id = :userId)")
    public Chat findChatBetween(Integer amigoId,Integer userId);

    @Query("SELECT cu.user FROM ChatUsuario cu WHERE cu.chat.id = :chatId")
    public List<User> findUsersByChat(Integer chatId);
    
}
