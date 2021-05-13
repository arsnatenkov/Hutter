package application.repository;

import application.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAll();

    List<Offer> findByAddress(String address);

    Optional<Offer> findById(Long publicId);

    List<Offer> findByHostId(Long hostId);

    List<Offer> findByCostBetween(Long low, Long high);

    List<Offer> findByQuantityRoom (Integer quantityRoom);

    @Query(value = "SELECT o FROM Offer o WHERE o.quantityRoom >= 4")
    List<Offer> findByQuantityRoomMoreFour();
}
