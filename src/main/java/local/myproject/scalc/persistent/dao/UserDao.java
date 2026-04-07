package local.myproject.scalc.persistent.dao;

import local.myproject.scalc.domain.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);
    Optional<User> findByUserName(String userName);
    User save(User user);
    User update(User user);
    void deleteById(long id);
}
