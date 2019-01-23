package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.junit.Assert;
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

import static org.mockito.Mockito.*;
import static ru.BlackAndWhite.CuteJavaPet.common.CreateThings.newUserList;

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

        Assert.assertNotNull(groupService.selectUsersByGroupId(1));
        Assert.assertArrayEquals(originalUserList.toArray(),
                groupService.selectUsersByGroupId(1).toArray());
    }

    @Test
    public void selectUsersByGroup() {
        List<User> originalUserList = newUserList(3);
        when(groupDAO.selectUsersByGroup(any(Group.class))).thenReturn(originalUserList);
        Assert.assertNotNull(groupService.selectUsersByGroup(new Group()));
        Assert.assertArrayEquals(originalUserList.toArray(),
                groupService.selectUsersByGroup(new Group()).toArray());
    }

    @Test
    public void selectGroup() {
    }

    @Test
    public void createGroup() {
    }

    @Test
    public void selectGroupsByUserId() {
    }

    @Test
    public void selectGroupsByUser() {
    }

    @Test
    public void selectGroupByName() {
    }

    @Test
    public void selectAllGroups() {
    }

    @Test
    public void delUserFromGroup() {
    }

    @Test
    public void addUserToGroup() {
    }

    @Test
    public void addUserToGroups() {
    }

    @Test
    public void delUserFromGroups() {
    }

    @Test
    public void getEmailsByGroups() {
    }

    @Test
    public void replaceGroupsOfUser() {
    }

    @Test
    public void setNewGroupListToUser() {
    }
}