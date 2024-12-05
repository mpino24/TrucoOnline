package es.us.dp1.lx_xy_24_25.truco_beasts.chat;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Integer>{
    
}
