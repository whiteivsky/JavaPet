package ru.BlackAndWhite.CuteJavaPet.services;

import ru.BlackAndWhite.CuteJavaPet.model.User;

public interface UserService {
    void save(User user);

    User findByUserName(String username);

    User findByUserEmail(String email);

    Object userRegistration(String userName, String email, String password, String confirmPassword);

    User getCurrentLoggedUser();
}
