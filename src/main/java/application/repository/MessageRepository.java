package application.repository;

import application.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value="SELECT m FROM Message m WHERE m.id IN " +
            "(SELECT MAX(id) FROM Message GROUP BY sender, receiver) " +
            "AND (m.sender.id = :id OR m.receiver.id = :id) AND (m.offerId = :offerId)")
    List<Message> findAllRecentMessages(@Param("id") Long id, @Param("offerId") Long offerId);

    @Query(value="SELECT m FROM Message m WHERE (m.sender.id = :userId " +
            "AND m.receiver.id = :companionId AND m.offerId = :offerId) " +
            "OR (m.sender.id = :companionId AND m.receiver.id = :userId " +
            "AND m.offerId = :offerId) ORDER BY m.time")
    List<Message> findConversation(@Param("userId") Long userId,
                                   @Param("companionId") Long companionId,
                                   @Param("offerId") Long offerId);

    List<Message> findByOfferId(Long offerId);

    Message findFirstBySenderIdOrReceiverIdOrderByIdDesc(Long userId, Long theSameUserId);
}
