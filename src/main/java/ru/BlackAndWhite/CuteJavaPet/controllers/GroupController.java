package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Controller

public class GroupController {
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    @GetMapping(path = "groups")
    public String manageUserGroups(ModelMap model) {
        model.addAttribute("isActiveGroup", "class=\"active\"")
                .addAttribute("groups",
                        getAllGroupsWithChecked(groupService.selectAllGroups(),
                                userService.getCurrentLoggedUser().getGroups()));
        return "groups";
    }

    @PostMapping(path = "groups")
    public String applyGroup(@RequestParam(value = "selectGroup", required = false) String[] applyGroupList,
                             @RequestParam(value = "addGroupName") String newGroupName,
                             ModelMap model) {
        model.addAttribute("Applgroups",
                groupService.setNewGroupListToUser(applyGroupList,
                        newGroupName,
                        userService.getCurrentLoggedUser()));
        return "redirect:/";
    }

    private List<GroupCheckedList> getAllGroupsWithChecked(List<Group> allGroups, List<Group> userGroups) {
        List<GroupCheckedList> groupCheckedLists = new ArrayList<>();
        try {

            for (Group allGroup : allGroups) {
                allGroup.setUsers(groupService.selectUsersByGroup(allGroup));
                groupCheckedLists.add(
                        new GroupCheckedList(allGroup, userGroups.contains(allGroup)));
            }
        } catch (IllegalArgumentException e) {
            for (Group allGroup : allGroups) {
                allGroup.setUsers(groupService.selectUsersByGroup(allGroup));
                groupCheckedLists.add(
                        new GroupCheckedList(allGroup, false));
            }
        }
        return groupCheckedLists;
    }

    @Data
    public class GroupCheckedList {
        private String groupName;
        private String checked;
        private List<String> users;

        GroupCheckedList(Group group, Boolean isChecked) {
            this.groupName = group.getGroupName();
            this.users = new ArrayList<>(convertUserNames(group.getUsers()));
            this.setChecked(isChecked);
        }

        List<String> convertUserNames(List<User> userNames) {
            List<String> stringUserNames = new ArrayList<>();
            if (userNames != null) {
                for (User user : userNames) {
                    stringUserNames.add(user.getUserName());
                }
            }
            return stringUserNames;
        }

        void setChecked(Boolean isChecked) {
            this.checked = isChecked ? "checked" : "";
        }

        @Override
        public String toString() {
            return groupName + "(" + checked + ") with users - " + (getUsers() != null ? getUsers().size() : "null");
        }

    }


}
