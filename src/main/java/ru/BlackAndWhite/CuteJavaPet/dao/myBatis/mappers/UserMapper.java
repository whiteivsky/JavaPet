package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.StringTypeHandler;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface UserMapper {

    @Results(id = "userMap", value = {
            @Result(property = "id", column = "ID", id = true),
            @Result(property = "userName", column = "username", typeHandler = StringTypeHandler.class),
            @Result(property = "password", column = "password", typeHandler = StringTypeHandler.class),
            @Result(property = "email", column = "email", typeHandler = StringTypeHandler.class),
            @Result(property = "roles", column = "id", javaType = List.class,
                    many = @Many(select = "ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.RoleMapper.selectRolesByUserId",
                            fetchType = FetchType.LAZY)),
            @Result(property = "groups", column = "id", javaType = List.class,
                    many = @Many(select = "ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.GroupMapper.selectGroupsByUserId",
                            fetchType = FetchType.LAZY))
    })

    @Select("SELECT * from users where username = #{userName}")
    User findByUsername(String userName);

    @Select("SELECT * from users where email = #{email}")
    User findByUserEmail(String email);

    @Select("SELECT * from users where id = #{id}")
    User selectUserByID(Integer id);

    @Insert("INSERT INTO users (id, username, password, email) VALUES (#{id},#{userName},#{password},#{email})")
    @Options(useGeneratedKeys = true)
    void insertUser(User user);
}

