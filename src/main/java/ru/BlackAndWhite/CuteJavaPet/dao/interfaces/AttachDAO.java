package ru.BlackAndWhite.CuteJavaPet.dao.interfaces;

import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface AttachDAO {
    void saveAttach(Attach attach) throws Exception;

    Attach selectAttachByID(Integer id) throws Exception;

    List<Attach> selectAttachesByID(Integer id) throws Exception;

    void addAttachGroups(Attach attach, List<Group> groups) throws Exception;

    void addAttachGroup(Attach attach, Group group) throws Exception;

    List<Attach> selectAttachesbyUser(User user) throws Exception;

    List<Attach> selectAttachesByGroupID(Integer id) throws Exception;

    List<Attach> selectAttachesByGroup(Group group) throws Exception;
}
