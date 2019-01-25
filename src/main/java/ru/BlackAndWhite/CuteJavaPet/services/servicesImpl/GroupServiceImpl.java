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
import java.util.List;

@Service
@Log4j
public class GroupServiceImpl implements GroupService {
    @Autowired
    UserDAO userDAO;

    @Autowired
    GroupDAO groupDAO;


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
        groupDAO.delUserFromGroup(user, group);
    }

    @Override
    public void addUserToGroup(User user, Group group) {
        groupDAO.addUserToGroup(user, group);
    }


    public void addUserToGroups(User user, List<Group> groupList) {
        for (Group curGroup : groupList) {
            groupDAO.addUserToGroup(user, curGroup);
        }
    }

    public void delUserFromGroups(User user, List<Group> groupList) {
        for (Group curGroup : groupList) {
            groupDAO.delUserFromGroup(user, curGroup);
        }
    }

    @Override
    public List<String> getEmailsByGroups(List<Group> groupList) {
        return groupDAO.getEmailsByGroups(groupList);
    }

    @Override
    public void replaceGroupsOfUser(User user, List<Group> newGroupList) {
        if (user == null) return;
        List<Group> userGroupList = groupDAO.selectGroupsByUserId(user.getId());
        if (userGroupList == null) {
            //No one group has been loaded
            addUserToGroups(user, newGroupList);
            return;
        }
        if (newGroupList == null) {
            //No one group has been selected - delete all
            delUserFromGroups(user, userGroupList);
            return;
        }
        if ((newGroupList.size() != userGroupList.size()) ||
                (!userGroupList.containsAll(newGroupList))) {
            delUserFromGroups(user, userGroupList);
            addUserToGroups(user, newGroupList);
        }
    }

    @Override
    public Object setNewGroupListToUser(String[] applyGroupList, String newGroupName, User currentLoggedUser) {
        ArrayList<String> status = new ArrayList<>();
        if (currentLoggedUser == null) return null;
        if (applyGroupList == null) {
            // The array is empty, remove user groups
            replaceGroupsOfUser(currentLoggedUser, null);
        } else {
            ArrayList<Group> selectedGroupList = new ArrayList<>();
            for (String item : applyGroupList) {
                selectedGroupList.add(groupDAO.selectGroupByName(item));
            }

            replaceGroupsOfUser(
                    currentLoggedUser, selectedGroupList);
            selectedGroupList.forEach(x -> status.add(x.getGroupName()));
        }

        // working with new group
        return addNewGroupToUser(newGroupName, currentLoggedUser, status);
    }

    private ArrayList addNewGroupToUser(String newGroupName, User currentLoggedUser, ArrayList<String> status) {
        if (newGroupName != null && !newGroupName.equals("")) {
            createGroup(newGroupName);
            addUserToGroup(currentLoggedUser,
                    groupDAO.selectGroupByName(newGroupName));
            status.add("add new group '" + newGroupName + "'");
        }
        return status;
    }
}
