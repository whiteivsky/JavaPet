package ru.BlackAndWhite.CuteJavaPet.dao.interfaces;

import ru.BlackAndWhite.CuteJavaPet.model.User;

public interface UserDAO {
    public User findByUsername(String userName) ;
    public User findByUserEmail(String email) ;
    public void insertUser(User user);
    public int getIdUser(String userName) ;
    public User selectUserByID(int id);
}
