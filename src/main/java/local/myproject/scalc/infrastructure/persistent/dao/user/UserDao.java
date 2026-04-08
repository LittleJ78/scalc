package local.myproject.scalc.infrastructure.persistent.dao.user;

import local.myproject.scalc.domain.aggregate.user.User;

import java.util.Optional;

/**
 * DAO для работы с пользователями.
 *
 * @author Evgenii Mironov
 */
public interface UserDao {
    /**
     * Ищет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return найденный пользователь или пустой результат
     */
    Optional<User> findById(long id);

    /**
     * Ищет пользователя по имени.
     *
     * @param userName имя пользователя
     * @return найденный пользователь или пустой результат
     */
    Optional<User> findByUserName(String userName);

    /**
     * Сохраняет пользователя.
     *
     * @param user пользователь для сохранения
     * @return сохраненный пользователь
     */
    User save(User user);

    /**
     * Обновляет пользователя.
     *
     * @param user пользователь для обновления
     * @return обновленный пользователь
     */
    User update(User user);

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return результат не возвращается
     */
    void deleteById(long id);
}
