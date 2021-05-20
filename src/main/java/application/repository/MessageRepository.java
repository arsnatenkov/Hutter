package application.repository;

import application.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Класс-репозиторий для работы с таблицей Сообщений
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByOfferId(@Param("offerId") Long offerId);

    List<Message> findMessageByRoomId(Long roomId);

    @Query(value = "SELECT m FROM Message m WHERE (m.sender.id = :userId AND m.offerId = :offerId)")
    List<Message> findMessageByUserIdAndOfferId(@Param("userId")Long userId,@Param("offerId") Long offerId);

    @Query(value = "SELECT m FROM Message m WHERE m.id > 0 ORDER BY m.roomId")
    List<Message> findAll();

    @Query(value = "SELECT m FROM Message m WHERE ((m.sender.id = :userId AND m.offerId = :offerId))")
    List<Message> findRoom(@Param("offerId") Long offerId, @Param("userId") Long userId);
}
