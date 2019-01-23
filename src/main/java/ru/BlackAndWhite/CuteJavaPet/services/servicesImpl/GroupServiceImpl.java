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
import java.util.stream.Stream;

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
    public void replaceGroupsOfUser(User user, List<Group> groupList) {
        List<Group> userGroupList = groupDAO.selectGroupsByUserId(user.getId());
        if (userGroupList == null) return;    //No one group has been loaded
        if (groupList == null) {              //No one group has been selected - delete all
            delUserFromGroups(user, userGroupList);
            return;
        }
        if ((groupList.size() != userGroupList.size()) ||
                (!userGroupList.containsAll(groupList))) {
            delUserFromGroups(user, userGroupList);
            addUserToGroups(user, groupList);
        }
    }

    public Object setNewGroupListToUser(String[] applyGroupList, String newGroupName, User currentLoggedUser) {
        List<String> status = new ArrayList<>();
        if (applyGroupList == null) {
            // The array is empty, remove user groups
            replaceGroupsOfUser(
                    currentLoggedUser, null);
            status.add("all groups were deleted");
            return status;
        }
        List<Group> selectedGroupList = Stream.of(applyGroupList)
                .collect(
                        () -> new ArrayList<>(),
                        (list, item) -> list.add(groupDAO.selectGroupByName(item)),
                        (list1, list2) -> list1.addAll(list2));

        replaceGroupsOfUser(
                currentLoggedUser, selectedGroupList);
        ((ArrayList) status).addAll(selectedGroupList);

        // working with new group
        addNewGroupToUser(newGroupName, currentLoggedUser, (ArrayList) status);
        return status;
    }

    private void addNewGroupToUser(String newGroupName, User currentLoggedUser, ArrayList status) {
        if (!newGroupName.equals("")) {
            createGroup(newGroupName);
            addUserToGroup(currentLoggedUser,
                    groupDAO.selectGroupByName(newGroupName));
            status.add("add new group '" + newGroupName + "'");
        }
    }
}
