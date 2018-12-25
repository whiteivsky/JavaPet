package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.NonNull;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.AttachDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j
@Service
@PropertySource(value = {"classpath:/email.properties", "classpath:/uploadStatuses.properties"})
public class AttachmentServiceImpl implements AttachmentService {
    @Resource
    Environment env;
    @Autowired
    AttachDAO attachDAO;
    @Autowired
    GroupDAO groupDAO;
    @Autowired
    UserService userService;
    @Autowired
    FileFormatServiceImpl fileFormatService;


    @Override
    public List<String> saveAttachments(String filedescription, MultipartFile[] files) {
        List<String> uploadStatuses = new ArrayList<>();
        for (MultipartFile fileData : Arrays.asList(files)) {
            uploadStatuses.add(saveAttach(filedescription, fileData));
        }
        return uploadStatuses;
    }

    private String saveAttach(String fileDescription, MultipartFile fileData) {
        if (fileData.isEmpty())
            return env.getProperty("empty") + "'" + fileData.getOriginalFilename() + "' is empty";
        if (fileFormatService.getIconByExt(fileData.getOriginalFilename()) == null)
            return env.getProperty("wrongFormat") + "'" + fileData.getOriginalFilename() + "' have wrong format";
        String uploadStatuses;
        try {
            Attach uploadAttachment = new Attach();
            uploadAttachment.setMediaType(fileData.getContentType());
            uploadAttachment.setMultiPartFileData(fileData);
            uploadAttachment.setDescription(fileDescription);
            uploadAttachment.setFileName(fileData.getOriginalFilename());
            uploadAttachment.setOwner(userService.getCurrentLoggedUser());
            attachDAO.saveAttach(uploadAttachment);
            attachDAO.addAttachGroups(uploadAttachment, groupDAO.selectGroupsByUserId(uploadAttachment.getOwner().getId()));

            uploadStatuses = env.getProperty("success") + "'" + uploadAttachment.getFileName() + "' upload done";
        } catch (Exception e) {
            log.error(e);
            uploadStatuses = e.getLocalizedMessage();
        }
        return uploadStatuses;
    }

    @Override
    public List<Attach> selectAttachmentsbyUser(User user) throws Exception {
        return attachDAO.selectAttachesbyUser(user);
    }

    @Override
    public Attach selectAttachmentByID(int id) throws Exception {
        return attachDAO.selectAttachByID(id);
    }


//    @Override
//    public List<Attach> selectAttachmentsByUserID(int id) throws Exception {
//        return attachDAO.selectAttachesByID(id);
//    }
//
//    @Override
//    public void addAttachmentGroups(Attach attach, List<Group> groupSet) {
//        if ((groupSet != null) & (attach != null)) {
//            groupSet.forEach(curGroup -> {
//                try {
//                    attachDAO.addAttachGroup(attach, curGroup);
//                } catch (Exception e) {
//                    log.error(e);
//                }
//            });
//        }
//    }
//    private void saveAttach(Attach attach) {
//        try {
//            attach.setOwner(userService.getCurrentLoggedUser());
//            attachDAO.saveAttach(attach);
//            attachDAO.addAttachGroups(attach,
//                    groupDAO.selectGroupsByUserId(attach.getOwner().getId()));
//        } catch (Exception e) {
//            log.error(e);
//        }
//    }
//
//    private Attach setAttach(String filedescription, MultipartFile fileData) throws IOException {
//        Attach uploadAttachment = new Attach();
//        uploadAttachment.setMediaType(fileData.getContentType());
//        uploadAttachment.setMultiPartFileData(fileData);
//        uploadAttachment.setDescription(filedescription);
//        uploadAttachment.setFileName(fileData.getOriginalFilename());
//        uploadAttachment.setOwner(userService.getCurrentLoggedUser());
//        return uploadAttachment;
//    }
}
