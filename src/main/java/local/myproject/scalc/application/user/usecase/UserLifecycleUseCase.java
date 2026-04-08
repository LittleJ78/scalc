package local.myproject.scalc.application.user.usecase;

import local.myproject.scalc.application.user.port.out.UserPort;
import local.myproject.scalc.domain.aggregate.user.Role;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dao.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Реализация сервиса работы с пользователями.
 *
 * @author Evgenii Mironov
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserLifecycleUseCase implements UserPort {
    private final UserDao userDao;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    /**
     * Возвращает пользователя по имени.
     *
     * @param userName имя пользователя
     * @return найденный пользователь
     */
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName).orElseThrow();
    }

    @Override
    /**
     * Сохраняет пользователя с кодированием пароля и назначением роли.
     *
     * @param user пользователь для сохранения
     * @return результат не возвращается
     */
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        userDao.save(user);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return результат не возвращается
     */
    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param user пользователь для обновления
     * @return результат не возвращается
     */
    public void updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User oldUser = userDao.findById(user.getUserId()).orElseThrow();
        user.setRoles(oldUser.getRoles());
        user.setProjects(oldUser.getProjects());
        userDao.update(user);
    }
}
