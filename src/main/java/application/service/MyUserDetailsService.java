package application.service;

import application.entity.Role;
import application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Класс для работы с доступом пользователя
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    /**
     * Метод загрузки пользователя по никнэйму
     * @param userName Никнейм
     * @return Класс деталей пользователя
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) {
        User user = userService.findUserByUserName(userName);
        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
        return buildUserForAuthentication(user, authorities);
    }

    /**
     * Метод получений роли пользователя
     * @param userRoles Множество ролей
     * @return Лист доступа
     */
    private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
        List<GrantedAuthority> roles = new ArrayList<>();

        for (Role role : userRoles)
            roles.add(new SimpleGrantedAuthority(role.getRole()));

        return new ArrayList<>(roles);
    }

    /**
     * Метод сборки пользователя для аутентификации
     * @param user Пользователь
     * @param authorities Лист доступа
     * @return Класс деталей пользователя
     */
    private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
        String userName = user.getUserName(), pwd = user.getPassword();

        return new org.springframework.security.core.userdetails
                .User(userName, pwd, user.getActive(),
                true, true, true, authorities);
    }
}