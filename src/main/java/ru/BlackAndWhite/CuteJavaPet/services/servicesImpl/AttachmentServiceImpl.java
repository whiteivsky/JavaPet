package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
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
import java.util.stream.Collectors;

@Log4j
@Service
@PropertySource(value ={"classpath:/email.properties","classpath:/uploadStatuses.properties"})
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


    private void saveAttach(Attach attach) {
        try {
            attach.setOwner(userService.getCurrentLoggedUser());
            attachDAO.saveAttach(attach);
            attachDAO.addAttachGroups(attach,
                    groupDAO.selectGroupsByUserId(attach.getOwner().getId()));
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void addAttachmentGroups(Attach attach, List<Group> groupSet) {
        if ((groupSet != null) & (attach != null)) {
            groupSet.forEach(curGroup -> {
                try {
                    attachDAO.addAttachGroup(attach, curGroup);
                } catch (Exception e) {
                    log.error(e);
                }
            });
        }
    }

    @Override
    public Attach selectAttachmentByID(int id) throws Exception {
        return attachDAO.selectAttachByID(id);
    }

    @Override
    public List<Attach> selectAttachmentsByUserID(int id) throws Exception {
        return attachDAO.selectAttachesByID(id);
    }

    @Override
    public List<Attach> selectAttachmentsbyUser(User user) throws Exception {
        return attachDAO.selectAttachesbyUser(user);
    }

    @Override
    public List<String> saveAttachments(String filedescription, MultipartFile[] files) {
        List<String> uploadStatuses = new ArrayList<>();
        List<MultipartFile> fileList = new ArrayList<>(Arrays.asList(files));

        fileList.removeAll(fileList.stream()
                .sequential()
                .filter(MultipartFile::isEmpty)
                .peek(file -> uploadStatuses.add(env.getProperty("empty")+"'" +
                        file.getOriginalFilename() + "' is empty"))
                .collect(Collectors.toList()));

        fileList.removeAll(fileList.stream()
                .sequential()
                .filter(file ->
                        (fileFormatService.getIconByExt(file.getOriginalFilename()) == null))
                .peek(file -> uploadStatuses.add(env.getProperty("wrongFormat")+"'" +
                        file.getOriginalFilename() + "' have wrong format"))
                .collect(Collectors.toList()));

        List<Attach> attachesForNotice = new ArrayList<>();
        if (!fileList.isEmpty()) {
            fileList.stream()
                    .sequential()
                    .forEach(fileData -> saveAttachAndNotice(filedescription, uploadStatuses, fileData, attachesForNotice));
        }
//        EmailServiceImpl.generateAndSendNotice(
//                env.getProperty("dLogin"),
//                env.getProperty("dPass"),
//                attachesForNotice,
//                groupDAO.getEmailsByGroups(userService.getCurrentLoggedUser().getGroups()));
        return uploadStatuses;
    }

    private void saveAttachAndNotice(String filedescription, List<String> uploadStatuses, MultipartFile fileData, List<Attach> attachesForNotice) {

        try {
            Attach uploadAttachment = setAttach(filedescription, fileData);
            saveAttach(uploadAttachment);
            if (uploadAttachment.getId() != 0) {
                attachesForNotice.add(uploadAttachment);
            }
            uploadStatuses.add(env.getProperty("success")+"'" + uploadAttachment.getFileName() + "' upload done");
        } catch (Exception e) {
            uploadStatuses.add(e.getLocalizedMessage());
        }

    }

    private Attach setAttach(String filedescription, MultipartFile fileData) throws IOException {
        Attach uploadAttachment = new Attach();
        uploadAttachment.setMediaType(fileData.getContentType());
        uploadAttachment.setMultiPartFileData(fileData);
        uploadAttachment.setDescription(filedescription);
        uploadAttachment.setFileName(fileData.getOriginalFilename());
        uploadAttachment.setOwner(userService.getCurrentLoggedUser());
        return uploadAttachment;
    }
}
