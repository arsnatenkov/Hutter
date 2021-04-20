package application.repository;

import application.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    List<Offer> findByAddress(String address);
    Offer findByPublicId(Integer id);
    List<Offer> findByCostBetween(Long low, Long high);
    Offer getOffersByPublicId(Integer publicId);
}
