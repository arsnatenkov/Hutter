package application.repository;

import application.entity.OfferSearch;
import application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferSearchRepository extends JpaRepository<OfferSearch, Long> {
    OfferSearch findOfferSearchById(Long id);
}