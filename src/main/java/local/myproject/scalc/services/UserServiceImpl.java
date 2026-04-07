package local.myproject.scalc.services;


import local.myproject.scalc.domain.Role;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.persistent.dao.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserDao userDao;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public User findByUserName(String userName) {
        return userDao.findByUserName(userName).orElseThrow();
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User oldUser = userDao.findById(user.getUserId()).orElseThrow();
        user.setRoles(oldUser.getRoles());
        user.setProjects(oldUser.getProjects());
        userDao.update(user);
    }
}
