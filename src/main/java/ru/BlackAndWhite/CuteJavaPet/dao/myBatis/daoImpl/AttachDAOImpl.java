package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.daoImpl;

import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.AttachDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.AttachMapper;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j
@Repository
public class AttachDAOImpl implements AttachDAO {
    @Autowired
    FileFormatDAO fileFormatDAO;
    @Autowired
    private SqlSession sqlSession;

    public void saveAttach(Attach attach) throws Exception {
        sqlSession.getMapper(AttachMapper.class).saveAttach(attach);
        sqlSession.commit();
    }

    public Attach selectAttachByID(Integer id) throws Exception {
        Attach selectAttachId = sqlSession.getMapper(AttachMapper.class).selectAttachByID(id);
        selectAttachId.setFileFormat(fileFormatDAO.getIconByFilename(selectAttachId.getFileName()));
        sqlSession.commit();
        return selectAttachId;
    }

    public List<Attach> selectAttachesByID(Integer id) throws Exception {
        List<Attach> selectAttachIdList = sqlSession.getMapper(AttachMapper.class).selectAttachesByOwnerId(id);
        for (Attach selectAttachId : selectAttachIdList) {
            selectAttachId.setFileFormat(fileFormatDAO.getIconByFilename(selectAttachId.getFileName()));
        }
        sqlSession.commit();
        return selectAttachIdList;
    }

    public void addAttachGroups(Attach attach, List<Group> groups) throws Exception {
        for (Group curGroup : groups) {
            addAttachGroup(attach, curGroup);
            attach.setAccessGroups(groups);
        }
    }

    public void addAttachGroup(Attach attach, Group group) throws Exception {
        sqlSession.getMapper(AttachMapper.class).addAttachGroup(attach.getId(), group.getId());
        sqlSession.commit();
    }

    public List<Attach> selectAttachesByGroupID(Integer id) throws Exception {
        List<Attach> attachList = sqlSession.getMapper(AttachMapper.class).selectAttachesByGroupId(id);
        return attachList;
    }
    public List<Attach> selectAttachesByGroup(Group group) throws Exception {
        return selectAttachesByGroupID(group.getId());
    }

    public List<Attach> selectAttachesbyUser(User user) throws Exception {
        Set<Attach> selectAttaches = new HashSet<>();
        selectAttaches.addAll(sqlSession.getMapper(AttachMapper.class).selectAttachesByOwnerId(user.getId()));
        for (Group curGroup : user.getGroups()) {
            selectAttaches.addAll(sqlSession.getMapper(AttachMapper.class).selectAttachesByGroupId(curGroup.getId()));
        }
        sqlSession.commit();
        for (Attach selectAttachId : selectAttaches) {
            selectAttachId.setFileFormat(fileFormatDAO.getIconByFilename(selectAttachId.getFileName()));
        }
        return new ArrayList<>(selectAttaches);
    }
}
