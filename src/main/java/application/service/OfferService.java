package application.service;

import application.entity.Offer;
import application.entity.Role;
import application.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> findByAddress(String address) {
        return offerRepository.findByAddress(address);
    }

    public Offer findById(Integer id) {
        return offerRepository.findById(id);
    }

    public List<Offer> findByHostId(Long hostId){
        return offerRepository.findByHostId(hostId);
    }

    public List<Offer> findByCostBetween(Long low, Long high) {
        return offerRepository.findByCostBetween(low, high);
    }

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public void deleteOffer(Offer offer){
        offerRepository.delete(offer);
    }
}