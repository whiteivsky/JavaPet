package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.daoImpl;

import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.GroupMapper;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j
@Repository
public class GroupDAOImpl implements GroupDAO {
    @Autowired
    private SqlSession sqlSession;

    public Group selectGroup(Integer id) {
        Group idGroup = sqlSession.getMapper(GroupMapper.class).selectGroup(id);
        sqlSession.commit();
        return idGroup;
    }

    public void createGroup(String groupName) {
        Group group = new Group();
        group.setGroupName(groupName);
        sqlSession.getMapper(GroupMapper.class).createGroup(group);
        sqlSession.commit();
    }

    public List<Group> selectGroupsByUserId(Integer id) {
        List<Group> groups = sqlSession.getMapper(GroupMapper.class).selectGroupsByUserId(id);
        sqlSession.commit();
        return groups;
    }

    public List<Group> selectGroupsByUser(User user) {
        List<Group> groups = sqlSession.getMapper(GroupMapper.class).selectGroupsByUserId(user.getId());
        sqlSession.commit();
        return groups;
    }

    public Group selectGroupByName(String name) {
        Group group = sqlSession.getMapper(GroupMapper.class).selectGroupByName(name);
        sqlSession.commit();
        return group;
    }

    public List<Group> selectAllGroups() {
        List<Group> groups = sqlSession.getMapper(GroupMapper.class).selectAllGroups();
        sqlSession.commit();
        return groups;
    }

    public void delUserGroup(User user, Group group) {
        sqlSession.getMapper(GroupMapper.class).delUserGroup(user.getId(), group.getId());
        sqlSession.commit();
    }

    public void addUserGroup(User user, Group group) {
        sqlSession.getMapper(GroupMapper.class).addUserGroup(user.getId(), group.getId());
        sqlSession.commit();
    }

    public List<User> selectUsersByGroupId(Integer id) {
        List<User> selectedUsers = sqlSession.getMapper(GroupMapper.class).selectUsersByGroupId(id);
        sqlSession.commit();
        return selectedUsers;
    }

    public List<User> selectUsersByGroup(Group group) {
        List<User> selectedUsers = sqlSession.getMapper(GroupMapper.class).selectUsersByGroupId(group.getId());
        sqlSession.commit();
        return selectedUsers;
    }

    @Override
    public List<String> getEmailsByGroups(List<Group> groupList) {
        Set<String> emailResult = new HashSet<>();
        for (Group group : groupList) {
            List<User> userList = selectUsersByGroup(group);
            for (User user : userList) {
                emailResult.add(user.getEmail());
            }
        }
        return new ArrayList<>(emailResult);
    }


}
