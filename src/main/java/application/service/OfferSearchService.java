package application.service;

import application.converter.UserToUserDto;
import application.dto.UserDTO;
import application.entity.OfferSearch;
import application.entity.Role;
import application.entity.User;
import application.exceptions.UserNotFoundException;
import application.repository.OfferSearchRepository;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class OfferSearchService {

    private OfferSearchRepository offerSearchRepository;

    public void saveOfferSearch(OfferSearch offerSearch) {
        offerSearchRepository.save(new OfferSearch("sasdasdas",
                Long.parseLong("0"), Long.parseLong("10000")));
//        offerSearchRepository.save(offerSearch);
    }
}
