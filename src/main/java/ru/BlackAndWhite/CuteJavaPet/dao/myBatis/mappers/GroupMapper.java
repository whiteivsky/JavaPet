package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.StringTypeHandler;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

//https://www.concretepage.com/mybatis-3/mybatis-3-annotation-example-with-select-insert-update-and-delete
public interface GroupMapper {
    /*    private Integer id;
    private String groupName;
    private Set<User> users;
    private Set<Attach> attaches;*/
    @Select("SELECT * FROM users left join users_groups on id=users_groups.user_id where group_id = #{id}")
    @ResultMap("ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.UserMapper.userMap")
    List<User> selectUsersByGroupId(Integer id);

    @Select("SELECT * FROM attachments left join attachments_groups on id=attachments_groups.attach_id where group_id = #{id}")
    @ResultMap("ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.AttachMapper.attachmentMap")
    List<Attach> selectAttachesByGroupId(Integer id);

    @Results(id = "groupMap", value = {
            @Result(property = "id", column = "ID", id = true),
            @Result(property = "groupName", column = "groupname", typeHandler = StringTypeHandler.class),
            @Result(property = "users", column = "id", javaType = List.class,
                    many = @Many(select = "selectUsersByGroupId",
                            fetchType = FetchType.LAZY)),
            @Result(property = "attaches", column = "id", javaType = List.class,
                    many = @Many(select = "selectAttachesByGroupID",
                            fetchType = FetchType.LAZY))
    })

    @Select("SELECT * FROM groups left join users_groups on id=users_groups.group_id where user_id = #{id}")
    List<Group> selectGroupsByUserId(Integer id);

    @Select("SELECT * from groups where ID = #{id}")
    Group selectGroup(Integer id);

    @Select("SELECT * from groups ")
    List<Group> selectAllGroups();

    @Select("SELECT * from groups where groups.groupname = #{groupName}")
    Group selectGroupByName(String groupName);

    @Insert("INSERT INTO groups VALUES (#{id}, #{groupName})")
    @Options(useGeneratedKeys = true)
    void createGroup(Group group);

    @Insert("INSERT INTO users_groups  VALUES (#{user_id}, #{group_id})")
    void addUserGroup(@Param("user_id") Integer user_id, @Param("group_id") Integer group_id);

    @Delete("DELETE FROM users_groups  WHERE ((users_groups.user_id = #{user_id}) and (users_groups.group_id = #{group_id}))")
    void delUserGroup(@Param("user_id") Integer user_id, @Param("group_id") Integer group_id);

}



