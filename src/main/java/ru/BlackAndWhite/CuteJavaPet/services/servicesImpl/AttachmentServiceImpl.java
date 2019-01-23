package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.AttachDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;
import ru.BlackAndWhite.CuteJavaPet.statuses.UploadStatusesWrapper;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
        List<String> list = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            String s = saveAttach(filedescription, multipartFile);
            list.add(s);
        }
        return list;
    }

    private String saveAttach(String fileDescription, MultipartFile fileData) {

        if (fileData.isEmpty())
            return UploadStatusesWrapper.getStatus(
                    UploadStatuses.EMPTY, fileData.getOriginalFilename());
        if (fileFormatService.getIconByFilename(fileData.getOriginalFilename()) == null)
            return UploadStatusesWrapper.getStatus(
                    UploadStatuses.WRONG_FORMAT, fileData.getOriginalFilename());
        return someSave(fileDescription, fileData);
    }

    private String someSave(String fileDescription, MultipartFile fileData) {
        try {
            Attach uploadAttachment = getAttach(fileDescription, fileData);
            attachDAO.saveAttach(uploadAttachment);
            attachDAO.addAttachGroups(uploadAttachment, groupDAO.selectGroupsByUserId(uploadAttachment.getOwner().getId()));
            return UploadStatusesWrapper.getStatus(UploadStatuses.SUCCESS, uploadAttachment.getFileName());
        } catch (Exception e) {
            log.error(e);
            return UploadStatusesWrapper.getStatus(UploadStatuses.UNKNOW, e.getLocalizedMessage());
        }
    }

    private Attach getAttach(String fileDescription, MultipartFile fileData) throws IOException {
        Attach uploadAttachment = new Attach();
        uploadAttachment.setMediaType(fileData.getContentType());
        uploadAttachment.setMultiPartFileData(fileData);
        uploadAttachment.setDescription(fileDescription);
        uploadAttachment.setFileName(fileData.getOriginalFilename());
        uploadAttachment.setOwner(userService.getCurrentLoggedUser());
        return uploadAttachment;
    }

    @Override
    public List<Attach> selectAttachmentsbyUser(User user) throws Exception {
        return attachDAO.selectAttachesbyUser(user);
    }

    @Override
    public Attach selectAttachmentByID(int id) throws Exception {
        return attachDAO.selectAttachByID(id);
    }
}
