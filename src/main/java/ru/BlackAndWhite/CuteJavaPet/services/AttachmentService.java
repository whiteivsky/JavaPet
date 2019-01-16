package ru.BlackAndWhite.CuteJavaPet.services;

import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.util.List;

public interface AttachmentService {
    List<String> saveAttachments(String filedescription, MultipartFile[] files);

    Attach selectAttachmentByID(int id) throws Exception;

    List<Attach> selectAttachmentsbyUser(User user) throws Exception;

//    List<Attach> selectAttachmentsByUserID(int id) throws Exception;
//    void addAttachmentGroups(Attach attach, List<Group> groupSet);
}