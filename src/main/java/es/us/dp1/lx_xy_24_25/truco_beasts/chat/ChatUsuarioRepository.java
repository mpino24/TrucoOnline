package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface ChatUsuarioRepository extends CrudRepository<ChatUsuario, Integer>{

    @Query("SELECT cu FROM ChatUsuario cu WHERE cu.user.id = :userId AND cu.chat.id = :chatId")
    public Optional<ChatUsuario> findChatUsuarioByUserAndChat(Integer userId, Integer chatId);

}
