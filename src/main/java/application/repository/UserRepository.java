package application.repository;

import application.entity.Offer;
import application.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Класс-репозиторий для работы с таблицей Пользователей
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    User findByEmailOrUserName(String email, String userName);

    @NonNull
    List<User> findAll();
}
