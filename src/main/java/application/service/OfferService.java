package application.service;

import application.entity.Offer;
import application.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
@Service
public class OfferService {
    private OfferRepository offerRepository;
    @Autowired
    public OfferService(OfferRepository offerRepository){
        this.offerRepository = offerRepository;
    }
    public List<Offer> findByAddress(String address){
        return offerRepository.findByAddress(address);
    }
    public Offer findByPublicId(Integer id){
        return offerRepository.findByPublicId(id);
    }
    public List<Offer> findByCostBetween(Long low, Long high){
        return offerRepository.findByCostBetween(low, high);
    }
    public Offer getOffersByPublicId(){
        return offerRepository.getOffersByPublicId(1);

    }
}
