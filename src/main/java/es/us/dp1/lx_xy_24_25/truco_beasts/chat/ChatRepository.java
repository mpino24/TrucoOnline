package es.us.dp1.lx_xy_24_25.truco_beasts.chat;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer>{
    
    @Query("SELECT c FROM Chat c WHERE " +
    "EXISTS (SELECT u FROM c.usuarios u WHERE u.id = :amigoId) AND " +
    "EXISTS (SELECT u FROM c.usuarios u WHERE u.id = :userId)")
    public Chat findChatBetween(Integer amigoId,Integer userId);
}
