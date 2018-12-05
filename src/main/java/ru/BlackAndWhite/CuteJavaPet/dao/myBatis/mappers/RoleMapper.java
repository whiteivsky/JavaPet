package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import ru.BlackAndWhite.CuteJavaPet.model.Role;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface RoleMapper {

    @Select("SELECT * FROM roles left join users_roles on id=users_roles.role_id where user_id = #{id}")
    List<Role> selectRolesByUserId(Integer id);

    @Select("SELECT * FROM users left join users_roles on id=users_roles.user_id where role_id = #{id}")
    List<User> selectUsersByRoleId(Integer id);

    @Insert("INSERT INTO users_roles  VALUES (#{userId}, #{roleId})")
    void addUserRole(@Param("userId") Integer userId, @Param("roleId")  Integer roleId);

    @Select("SELECT * FROM roles where name = #{name}")
    List<Role> selectRolesByName(@Param("name") String name);

    @Insert("INSERT INTO roles(id, name) VALUES (#{id},#{name})")
    @Options(useGeneratedKeys = true)
    void addRole(Role role);
}
