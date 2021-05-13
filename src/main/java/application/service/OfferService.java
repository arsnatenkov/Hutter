package application.service;

import application.entity.Offer;
import application.entity.Role;
import application.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> findByAddress(String address) {
        return offerRepository.findByAddress(address);
    }

    public Optional<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }

    public List<Offer> findByHostId(Long hostId) {
        return offerRepository.findByHostId(hostId);
    }

    public List<Offer> findByCostBetween(Long low, Long high) {
        return offerRepository.findByCostBetween(low, high);
    }

    public List<Offer> findByQuantityRoom(Integer quantityRoom) {
        return offerRepository.findByQuantityRoom(quantityRoom);
    }

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public void saveOffer(Offer offer) {
        offerRepository.save(offer);
    }

    public void deleteOffer(Offer offer) {
        offerRepository.delete(offer);
    }

    public List<Offer> findByQuantityRoomMoreFour(){
        return offerRepository.findByQuantityRoomMoreFour();
    }
}