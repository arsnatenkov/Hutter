package application.repository;

import application.entity.OfferSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferSearchRepository extends JpaRepository<OfferSearch, Long> {
    Optional<OfferSearch> findById(Long id);
}