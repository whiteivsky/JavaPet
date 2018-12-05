package ru.BlackAndWhite.CuteJavaPet.dao.interfaces;

import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface GroupDAO {
    Group selectGroup(Integer id);

    void createGroup(String groupName);

    List<Group> selectGroupsByUserId(Integer id);

    List<Group> selectGroupsByUser(User user);

    Group selectGroupByName(String name);

    List<Group> selectAllGroups();

    void delUserGroup(User user, Group group);

    void addUserGroup(User user, Group group);

    List<User> selectUsersByGroupId(Integer id);

    List<User> selectUsersByGroup(Group group);

    List<String> getEmailsByGroups(List<Group> groupList);
}

