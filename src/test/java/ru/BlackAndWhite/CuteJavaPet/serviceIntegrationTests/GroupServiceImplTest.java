package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.GroupServiceImpl;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.BlackAndWhite.CuteJavaPet.common.CreateThings.*;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class GroupServiceImplTest {
    @Mock
    GroupDAO groupDAO;
    @Autowired
    @InjectMocks
    private GroupServiceImpl groupService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void selectUsersByGroupId() throws Exception {
        List<User> originalUserList = newUserList(3);
        when(groupDAO.selectUsersByGroupId(anyInt())).thenReturn(originalUserList);
        List<User> resultUsers = groupService.selectUsersByGroupId(1);
        assertNotNull(resultUsers);
        assertArrayEquals(originalUserList.toArray(), resultUsers.toArray());
    }

    @Test
    public void selectUsersByGroup() {
        List<User> originalUserList = newUserList(3);
        when(groupDAO.selectUsersByGroup(any(Group.class))).thenReturn(originalUserList);
        List<User> resultUsers = groupService.selectUsersByGroup(new Group());
        assertNotNull(resultUsers);
        assertArrayEquals(originalUserList.toArray(), resultUsers.toArray());
    }

    @Test
    public void selectGroup() {
        Group originalGroup = newGroup(1);
        when(groupDAO.selectGroup(anyInt())).thenReturn(originalGroup);
        Group resultGroup = groupService.selectGroup(1);
        assertNotNull(resultGroup);
        assertEquals(originalGroup, resultGroup);
    }

    @Test
    public void createGroup() {
        groupService.createGroup("newGroup");
        verify(groupDAO).createGroup("newGroup");
        verifyNoMoreInteractions(groupDAO);
    }

    @Test
    public void selectGroupsByUserId() {
        List<Group> originalGroups = newGroupList(3);
        when(groupDAO.selectGroupsByUserId(anyInt())).thenReturn(originalGroups);
        List<Group> resultGroups = groupService.selectGroupsByUserId(1);
        assertNotNull(resultGroups);
        assertEquals(originalGroups, resultGroups);
    }

    @Test
    public void selectGroupsByUser() {
        List<Group> originalGroups = newGroupList(3);
        when(groupDAO.selectGroupsByUser(any(User.class))).thenReturn(originalGroups);
        List<Group> resultGroups = groupService.selectGroupsByUser(new User());
        assertNotNull(resultGroups);
        assertEquals(originalGroups, resultGroups);
    }

    @Test
    public void selectGroupByName() {
        Group originalGroup = newGroup(1);
        when(groupDAO.selectGroupByName(anyString())).thenReturn(originalGroup);
        Group resultGroup = groupService.selectGroupByName("newGroup");
        assertNotNull(resultGroup);
        assertEquals(originalGroup, resultGroup);
    }

    @Test
    public void selectAllGroups() {
        groupService.selectAllGroups();
        verify(groupDAO).selectAllGroups();
        verifyNoMoreInteractions(groupDAO);
    }

    @Test
    public void delUserFromGroup() {
        groupService.delUserFromGroup(new User(), new Group());
        verify(groupDAO).delUserFromGroup(any(User.class), any(Group.class));
        verifyNoMoreInteractions(groupDAO);
    }

    @Test
    public void addUserToGroup() {
        groupService.addUserToGroup(new User(), new Group());
        verify(groupDAO).addUserToGroup(any(User.class), any(Group.class));
        verifyNoMoreInteractions(groupDAO);
    }

    @Test
    public void addUserToGroups() {
        List<Group> originalGroups = newGroupList(3);
        doNothing().when(groupDAO).addUserToGroup(any(User.class), any(Group.class));
        groupService.addUserToGroups(newUser(), originalGroups);
        verify(groupDAO, times(3)).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void delUserFromGroups() {
        List<Group> originalGroups = newGroupList(3);
        doNothing().when(groupDAO).delUserFromGroup(any(User.class), any(Group.class));
        groupService.delUserFromGroups(newUser(), originalGroups);
        verify(groupDAO, times(3)).delUserFromGroup(any(User.class), any(Group.class));
    }

    @Test
    public void getEmailsByGroups() {
//        List<String> originalEmails =new ArrayList<>();
//        for (Group curGroup : newGroupList(3)) {
//originalEmails.add(curGroup.)
//        }
    }

    @Test
    public void replaceGroupsOfUser() {
    }

    @Test
    public void setNewGroupListToUser() {
    }
}