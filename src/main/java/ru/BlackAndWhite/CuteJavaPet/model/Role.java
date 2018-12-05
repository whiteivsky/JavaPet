package ru.BlackAndWhite.CuteJavaPet.model;

import lombok.Data;

import java.util.Set;

@Data
public class Role {
    private Integer id;
    private String name;
    private Set<User> users;
}
