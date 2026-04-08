package local.myproject.scalc.infrastructure.security;

import local.myproject.scalc.domain.aggregate.user.Role;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dao.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Реализация {@link UserDetailsService} для загрузки пользователей из хранилища.
 *
 * @author Evgenii Mironov
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserDao userDao;

    @Override
    /**
     * Загружает пользователя по имени для Spring Security.
     *
     * @param username имя пользователя
     * @return объект пользовательских данных Spring Security
     */
    public UserDetails loadUserByUsername(String username) {
        Optional<User> optionalUser = userDao.findByUserName(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User with such username do not exists");
        }
        User user = optionalUser.get();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            log.info("Role {}",role.getAuthority());
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }
}
