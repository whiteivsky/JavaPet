package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;
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

    //default variable
    private List<User> originalUserList = newUserList(3);
    private List<Group> default3Groups = newGroupList(3);
    private Group originalGroup = newGroup(1);
    private User originalUser = newUser(1);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(groupDAO.selectUsersByGroupId(anyInt())).thenReturn(originalUserList);
        when(groupDAO.selectUsersByGroup(any(Group.class))).thenReturn(originalUserList);
        when(groupDAO.selectGroup(anyInt())).thenReturn(originalGroup);
        when(groupDAO.selectGroupsByUserId(anyInt())).thenReturn(default3Groups);
        when(groupDAO.selectGroupsByUser(any(User.class))).thenReturn(default3Groups);
        when(groupDAO.selectGroupByName(anyString())).thenReturn(originalGroup);
        doNothing().when(groupDAO).addUserToGroup(any(User.class), any(Group.class));
        doNothing().when(groupDAO).delUserFromGroup(any(User.class), any(Group.class));
    }

    @Test
    public void selectUsersByGroupId() throws Exception {
        List<User> resultUsers = groupService.selectUsersByGroupId(1);

        assertArrayEquals(originalUserList.toArray(), resultUsers.toArray());
    }

    @Test
    public void selectUsersByGroup() {
        List<User> resultUsers = groupService.selectUsersByGroup(new Group());

        assertArrayEquals(originalUserList.toArray(), resultUsers.toArray());
    }

    @Test
    public void selectGroup() {
        Group resultGroup = groupService.selectGroup(1);

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
        List<Group> resultGroups = groupService.selectGroupsByUserId(1);
        assertEquals(default3Groups, resultGroups);
    }

    @Test
    public void selectGroupsByUser() {
        List<Group> resultGroups = groupService.selectGroupsByUser(new User());
        assertEquals(default3Groups, resultGroups);
    }

    @Test
    public void selectGroupByName() {
        Group resultGroup = groupService.selectGroupByName("newGroup");
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
        groupService.addUserToGroups(newUser(), default3Groups);
        verify(groupDAO, times(3)).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void delUserFromGroups() {
        groupService.delUserFromGroups(newUser(), default3Groups);
        verify(groupDAO, times(3)).delUserFromGroup(any(User.class), any(Group.class));
    }

    @Test
    public void getEmailsByGroups() {
        groupService.getEmailsByGroups(default3Groups);
        verify(groupDAO).getEmailsByGroups(anyListOf(Group.class));
        verifyNoMoreInteractions(groupDAO);
    }

    @Test
    //User=null
    public void replaceGroupsOfUser_AsNull() {
        groupService.replaceGroupsOfUser(null, default3Groups);
        verifyZeroInteractions(groupDAO);
    }

    @Test
    //User haven`t groups
    public void replaceGroupsOfUser() {
        GroupService newGS = setUpMocksDAO(null);
        newGS.replaceGroupsOfUser(originalUser, default3Groups);
        verify(newGS, never()).delUserFromGroups(any(User.class), anyListOf(Group.class));
        verify(newGS, times(1)).addUserToGroups(originalUser, default3Groups);
    }

    @Test
    //No one group has been selected - delete all
    public void replaceGroupsOfUser_WithoutGroups() {
        GroupService newGS = setUpMocksDAO(default3Groups);
        newGS.replaceGroupsOfUser(originalUser, null);
        verify(newGS, times(1)).delUserFromGroups(any(User.class), anyListOf(Group.class));
        verify(newGS, never()).addUserToGroups(any(User.class), anyListOf(Group.class));
    }

    @Test
    //No one group has been selected - delete all
    public void replaceGroupsOfUser_WithSameGroups() {
        GroupService newGS = setUpMocksDAO(default3Groups);
        newGS.replaceGroupsOfUser(originalUser, default3Groups);
        verify(newGS, never()).delUserFromGroups(any(User.class), anyListOf(Group.class));
        verify(newGS, never()).addUserToGroups(any(User.class), anyListOf(Group.class));
    }

    @Test
    //No one group has been selected - delete all
    public void replaceGroupsOfUser_WithDifferentGroups() {
        GroupService newGS = setUpMocksDAO(default3Groups);
        newGS.replaceGroupsOfUser(originalUser, newGroupList(2));
        verify(newGS, times(1)).delUserFromGroups(originalUser, default3Groups);
        verify(newGS, times(1)).addUserToGroups(originalUser, newGroupList(2));
    }

    @NotNull
    public GroupService setUpMocksDAO(List<Group> o) {
        GroupService newGS = Mockito.spy(groupService);
        when(groupDAO.selectGroupsByUserId(anyInt())).thenReturn(o);
        if (o != null)
            o.forEach(x -> when(groupDAO.selectGroupByName(x.getGroupName())).thenReturn(x));

        return newGS;
    }

    @Test
    public void setNewGroupListToUser_NullNewGroupList_NullNewGroup() {
        GroupService newGS = setUpMocksDAO(default3Groups);

        assertNotNull(newGS.setNewGroupListToUser(null, null, originalUser));

        verify(newGS, times(1)).replaceGroupsOfUser(originalUser, null);
        verify(newGS, never()).createGroup(any());
        verify(groupDAO, never()).selectGroupByName(anyString());
        verify(newGS, never()).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void setNewGroupListToUser_EmptyUser() {
        GroupService newGS = setUpMocksDAO(default3Groups);

        assertNull(newGS.setNewGroupListToUser(new String[]{"first", "second"}, null, null));
        verify(newGS, never()).replaceGroupsOfUser(originalUser, null);
        verify(groupDAO, never()).selectGroupByName(anyString());
        verify(newGS, never()).createGroup(any());
        verify(newGS, never()).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void setNewGroupListToUser_EmptyNewGroup() {
        GroupService newGS = setUpMocksDAO(default3Groups);

        newGS.setNewGroupListToUser(null, "", originalUser);
        verify(newGS, times(1)).replaceGroupsOfUser(originalUser, null);
        verify(groupDAO, never()).selectGroupByName(anyString());
        verify(newGS, never()).createGroup(any());
        verify(newGS, never()).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void setNewGroupListToUser_2Groups() {
        GroupService newGS = setUpMocksDAO(default3Groups);

        newGS.setNewGroupListToUser(new String[]{"first", "second"}, null, originalUser);
        verify(newGS, times(1)).replaceGroupsOfUser(any(User.class), anyListOf(Group.class));
        verify(newGS, never()).createGroup(any());
        verify(newGS, never()).addUserToGroup(any(User.class), any(Group.class));
    }

    @Test
    public void setNewGroupListToUser_NewGroup() {
        GroupService newGS = setUpMocksDAO(default3Groups);

        newGS.setNewGroupListToUser(null, "newGroupName", originalUser);
        verify(newGS, times(1)).replaceGroupsOfUser(originalUser, null);
        verify(groupDAO, times(1)).selectGroupByName(anyString());
        verify(newGS, times(1)).createGroup(any());
        verify(newGS, times(1)).addUserToGroup(any(User.class), any(Group.class));
    }
}