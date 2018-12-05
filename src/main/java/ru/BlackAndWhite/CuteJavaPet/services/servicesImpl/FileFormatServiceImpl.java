package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.services.FileFormatService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Service
public class FileFormatServiceImpl implements FileFormatService {

    @Autowired
    FileFormatDAO fileFormatDAO;

    @Override
    public List<String> save(MultipartFile[] files) {

        List<String> uploadStatuses = new ArrayList<>();

        for (MultipartFile fileData : files) {
            if (!fileData.isEmpty()) {
                try {

                    if (!fileData.getOriginalFilename().endsWith(".png")) {
                        throw new Exception("Error! '" + fileData.getOriginalFilename() + "' doesn't end with .png");
                    }

                    FileFormat format = new FileFormat();
//                    format.setIcon(fileData.getInputStream());
                    byte[] encodeBase64 = Base64.encodeBase64( fileData.getBytes());
                    String base64Encoded = null;
                    try {
                        base64Encoded = new String(encodeBase64, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        throw new Exception("Error! '" + fileData.getOriginalFilename() + "' doesn't encode to Base64");
                    }
                    format.setIcon(base64Encoded);

                    format.setMediaType(fileData.getContentType());
                    format.setName(fileData.getOriginalFilename().replace(".png", ""));

                    fileFormatDAO.save(format);
                    uploadStatuses.add("'" + format.getName() + "' upload done");
                } catch (Exception e) {
                    uploadStatuses.add(e.getLocalizedMessage());
                }
            } else {
                uploadStatuses.add("Error! '" + fileData.getOriginalFilename() + "' is empty");
            }
        }
          return  uploadStatuses;
    }

    @Override
    public FileFormat getIconByExt(String fileName) {
        return fileFormatDAO.getIconByFilename(fileName);
    }

    @Override
    public List<FileFormat> selectIcons() {
        return fileFormatDAO.getAllIcons();
    }
}
