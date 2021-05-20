package application.repository;

import application.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс-репозиторий для работы с таблицей Избранных
 */
@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findByUserId(Long userId);
    List<Favourite> findByOfferId(Long userId);
}
