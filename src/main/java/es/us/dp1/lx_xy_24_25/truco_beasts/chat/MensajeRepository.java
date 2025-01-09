package es.us.dp1.lx_xy_24_25.truco_beasts.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends CrudRepository<Mensaje, Integer>{
 
    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId AND m.fechaEnvio = (SELECT MAX(m2.fechaEnvio) FROM Mensaje m2 WHERE m2.chat.id = :chatId)")
    public Optional<Mensaje> findLastMessage(Integer chatId);

    @Query("SELECT m FROM Mensaje m WHERE m.chat.id = :chatId ")
    public List<Mensaje> findMessagesFrom(Integer chatId);

    @Query("SELECT COUNT(m) FROM Mensaje m WHERE m.chat.id = :chatId AND m.fechaEnvio > :fecha AND m.remitente.id != :userId")
    public Integer findMessagesAfter(Integer chatId, LocalDateTime fecha, Integer userId);
    

}