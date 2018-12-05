package ru.BlackAndWhite.CuteJavaPet.model;

import lombok.Data;

import java.util.List;

@Data
public class User {
    private Integer id;
    private String userName;
    private String password;
    private String email;
    private List<Group> groups;
    private List<Role> roles;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
