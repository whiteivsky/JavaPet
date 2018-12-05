package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.RoleDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.UserDAO;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.SecurityService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;
import ru.BlackAndWhite.CuteJavaPet.validator.UserValidator;

@Log4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserDAO userDao;
    @Autowired
    RoleDAO roleDao;
    @Autowired
    SecurityService securityService;
    @Autowired
    UserValidator userValidator;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.insertUser(user);
        roleDao.addDefaultUserRole(userDao.getIdUser(user.getUserName()));
        log.info("User (" + user.getUserName() + ") was saved");
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByUserEmail(String email) {
        return userDao.findByUserEmail(email);
    }

    @Override
    public Object userRegistration(String userName, String email, String password, String confirmPassword) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setEmail(email);

        String errors = userValidator.validateUser(user, confirmPassword);
        if (errors != null) {
            log.error("registration error: " + errors);
            return errors;
        }

        save(user);
        try {
            securityService.autoLogin(user.getUserName(), confirmPassword);
        } catch (Exception e) {
            return e;
        }
        return "Success registration";
    }

    @Override
    public User getCurrentLoggedUser() {
        return findByUserName(securityService.getCurrentLoggedUsername());
    }
}
