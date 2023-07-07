package local.myproject.scalc.services;

import local.myproject.scalc.entitys.User;
import local.myproject.scalc.repositories.UserRepository;

public interface UserService {
    User findByUserName(String userName);

    void save(User user);

    void deleteById(long id);

    void updateUser(User user);
}
