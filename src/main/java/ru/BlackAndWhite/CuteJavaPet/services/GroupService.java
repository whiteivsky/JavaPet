package ru.BlackAndWhite.CuteJavaPet.services;
//GroupService.selectUsersByGroupId
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface GroupService  {
    Group selectGroup(Integer id);
    void createGroup(String groupName);
    List<Group> selectGroupsByUserId(Integer id);
    List<Group> selectGroupsByUser(User user);
    Group selectGroupByName(String name);
    List<Group> selectAllGroups();
    void delUserFromGroup(User user, Group group);
    void addUserToGroup(User user, Group group);
    void addUserToGroups(User user, List<Group> groupList);
    void delUserFromGroups(User user, List<Group> groupList);
    void replaceGroupsOfUser(User user, List<Group> groupList);
    List<User> selectUsersByGroupId(Integer id);
    List<User> selectUsersByGroup(Group group);
    Object setNewGroupListToUser(String[] applyGroupList, String newGroupName, User currentLoggedUser);
    List<String> getEmailsByGroups(List<Group> group);
}
