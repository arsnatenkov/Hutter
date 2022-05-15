package application.service;

import application.converter.UserToUserDto;
import application.dto.UserDTO;
import application.entity.Offer;
import application.entity.Role;
import application.entity.User;
import application.exceptions.UserNotFoundException;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Сервис для репозитория Пользователь
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserToUserDto userToUserDto;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmailOrUserName(String email, String userName) {
        return userRepository.findByEmailOrUserName(email, userName);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("VISITOR");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        userRepository.save(user);
    }

    public void banUser(User user) {
        user.setActive(false);
        userRepository.save(user);
    }

    public void unbanUser(User user) {
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getUser(Long id) {
        String message = "User with id = " + id + " is not found";
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(message));
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        return userToUserDto.convert(getUser(id));
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
