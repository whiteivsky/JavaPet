package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.services.FileFormatService;
import ru.BlackAndWhite.CuteJavaPet.statuses.UploadStatusesWrapper;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class FileFormatServiceImpl implements FileFormatService {

    @Autowired
    FileFormatDAO fileFormatDAO;

    @Override
    public List<String> upload(MultipartFile[] files) {
        List<String> uploadStatuses = new ArrayList<>();
        if (files == null) return null;
        for (MultipartFile fileData : files) {
            uploadStatuses.add(fileParametersCheckAndSave(fileData));
        }
        return uploadStatuses;
    }

    public String fileParametersCheckAndSave(MultipartFile fileData) {
        if (fileData.isEmpty())
            return UploadStatusesWrapper.getStatus(UploadStatuses.EMPTY, fileData.getOriginalFilename());
        if (!fileData.getOriginalFilename().endsWith(".png"))
            return UploadStatusesWrapper.getStatus(UploadStatuses.WRONG_FORMAT, fileData.getOriginalFilename());
        try {
            fileFormatDAO.save(getFileFormat(fileData));
            return UploadStatusesWrapper.getStatus(UploadStatuses.SUCCESS, fileData.getOriginalFilename());
        } catch (UnsupportedEncodingException e) {
            return UploadStatusesWrapper.getStatus(UploadStatuses.BAD_ENCODE, fileData.getOriginalFilename());
        } catch (Exception e) {
            return e.getLocalizedMessage();
            //todo Вставить обработчик неожиданных ошибок
        }
    }

    public FileFormat getFileFormat(MultipartFile fileData) throws IOException {
        FileFormat format = new FileFormat();
        byte[] encodeBase64 = Base64.encodeBase64(fileData.getBytes());
        String base64Encoded = new String(encodeBase64, "UTF-8");
        format.setIcon(base64Encoded);
        format.setMediaType(fileData.getContentType());
        format.setName(fileData.getOriginalFilename().replace(".png", ""));
        return format;
    }

    @Override
    public FileFormat getIconByFilename(String fileName) {
        return fileFormatDAO.getIconByFilename(fileName);
    }

    @Override
    public List<FileFormat> getAllIcons() {
        return fileFormatDAO.getAllIcons();
    }

    @Override
    public void deleteIconByFilename(String name) {
        fileFormatDAO.deleteIconByFilename(name);
    }
}
