package ru.BlackAndWhite.CuteJavaPet.dao.interfaces;

import ru.BlackAndWhite.CuteJavaPet.model.Role;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface RoleDAO {
    public List<Role> selectRolesByUserId(Integer id);
    public List<User> selectUsersByRoleId(Integer id);
    public void addDefaultUserRole(Integer idUser);
    public void addUserRole(Integer idUser, Integer idRole);
}
