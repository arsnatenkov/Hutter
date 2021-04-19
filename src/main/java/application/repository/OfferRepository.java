package application.repository;

import application.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    Offer findByAddress(String address);
    Offer findByPublicId(Integer id);
    Offer findByCostBetween(Long low, Long high);
}
