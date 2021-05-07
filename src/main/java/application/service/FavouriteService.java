package application.service;

import application.entity.Favourite;
import application.repository.FavouriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteService {
    @Autowired
    private FavouriteRepository favouriteRepository;

    public List<Favourite> findByUserId(Long userId){
        return favouriteRepository.findByUserId(userId);
    }
}
