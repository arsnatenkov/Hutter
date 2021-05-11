package application.repository;

import application.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findAll();

    List<Offer> findByAddress(String address);

    Offer findById(Integer publicId);

    List<Offer> findByHostId(Long hostId);

    List<Offer> findByCostBetween(Long low, Long high);

    List<Offer> findByQuantityRoom (Integer quantityRoom);

    @Query(value = "SELECT o FROM Offer o WHERE o.quantityRoom >= 4")
    List<Offer> findByQuantityRoomMoreFour();
}
