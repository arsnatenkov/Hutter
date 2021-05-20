package application.service;

import application.entity.Favourite;
import application.repository.FavouriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для репозитория Избранных
 */
@Service
@RequiredArgsConstructor
public class FavouriteService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    public List<Favourite> findByUserId(Long userId){
        return favouriteRepository.findByUserId(userId);
    }

    public List<Favourite> findByOfferId(Long offerId){
        return favouriteRepository.findByOfferId(offerId);
    }

    public void deleteFavourite(Favourite favourite){
        favouriteRepository.delete(favourite);
    }

    public void saveFavourite(Favourite favourite){
        favouriteRepository.save(favourite);
    }
}
