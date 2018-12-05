package ru.BlackAndWhite.CuteJavaPet.services;


public interface SecurityService {
    void autoLogin(String username, String password) throws Exception;

    String getCurrentLoggedUsername();
}
