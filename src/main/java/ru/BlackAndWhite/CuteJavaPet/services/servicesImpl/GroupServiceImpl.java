package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.UserDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Log4j
public class GroupServiceImpl implements GroupService {
    final
    GroupDAO groupDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }


    @Override
    public List<User> selectUsersByGroupId(Integer id) {
        return groupDAO.selectUsersByGroupId(id);
    }

    @Override
    public List<User> selectUsersByGroup(Group group) {
        return groupDAO.selectUsersByGroup(group);
    }

    @Override
    public Group selectGroup(Integer id) {
        return groupDAO.selectGroup(id);
    }

    @Override
    public void createGroup(String groupName) {
        groupDAO.createGroup(groupName);
    }

    @Override
    public List<Group> selectGroupsByUserId(Integer id) {
        return groupDAO.selectGroupsByUserId(id);
    }

    @Override
    public List<Group> selectGroupsByUser(User user) {
        return groupDAO.selectGroupsByUser(user);
    }

    @Override
    public Group selectGroupByName(String name) {
        return groupDAO.selectGroupByName(name);
    }

    @Override
    public List<Group> selectAllGroups() {
        return groupDAO.selectAllGroups();
    }

    @Override
    public void delUserFromGroup(User user, Group group) {
        groupDAO.delUserGroup(user, group);
    }

    @Override
    public void addUserToGroup(User user, Group group) {
        groupDAO.addUserGroup(user, group);
    }


    public void addUserToGroups(User user, List<Group> groupList) {
        for (Group curGroup : groupList) {
            groupDAO.addUserGroup(user, curGroup);
        }
    }

    public void delUserFromGroups(User user, List<Group> groupList) {
        for (Group curGroup : groupList) {
            groupDAO.delUserGroup(user, curGroup);
        }
    }

    @Override
    public List<String> getEmailsByGroups(List<Group> groupList) {
        return groupDAO.getEmailsByGroups(groupList);
    }

    @Override
    public void replaceGroupsOfUser(User user, List<Group> groupList) {
        List<Group> userGroupList = groupDAO.selectGroupsByUserId(user.getId());
        if (userGroupList == null) {
            //log.info("No one group has been loaded");
            return;
        }
        if (groupList == null) {
            //log.info("No one group has been selected - delete all");
            delUserFromGroups(user, userGroupList);
            return;
        }

        if (groupList.size() == userGroupList.size()) {
            groupList.sort(Comparator.comparing(Group::getId));
            userGroupList.sort(Comparator.comparing(Group::getId));
            for (int i = 1; i < groupList.size(); i++) {
                if (!(groupList.get(i).equals(userGroupList.get(i)))) {
                    //log.info("Group lists were not same - delete all groups and add selected");
                    delUserFromGroups(user, userGroupList);
                    addUserToGroups(user, groupList);
                    break;
                }
            }
        } else {
           // log.info("Group lists were not same - delete all groups and add selected");
            delUserFromGroups(user, userGroupList);
            addUserToGroups(user, groupList);
        }
    }

    public Object setNewGroupListToUser(String[] applyGroupList, String newGroupName, User currentLoggedUser) {
        Object status = new ArrayList<>();
        if (applyGroupList != null) {
            List<Group> selectedGroupList = new ArrayList<>();
            for (int i = 0; i < applyGroupList.length; i++) {
                selectedGroupList.add(groupDAO.selectGroupByName(applyGroupList[i]));

            }
            //selectedGroupList.forEach(log::info);
            // Change user`s groups to selected
            if (selectedGroupList.size() != 0) {
                replaceGroupsOfUser(
                        currentLoggedUser, selectedGroupList);
                ((ArrayList) status).addAll(selectedGroupList);
            }
        } else {
            // The array is empty, remove user groups
            replaceGroupsOfUser(
                    currentLoggedUser, null);
            ((ArrayList) status).add("all groups were deleted");
        }
        // working with new group
        if (!newGroupName.equals("")) {
            createGroup(newGroupName);
            addUserToGroup(currentLoggedUser,
                    groupDAO.selectGroupByName(newGroupName));
            ((ArrayList) status).add("add new group '" + newGroupName + "'");
        }
        return status;
    }
}
