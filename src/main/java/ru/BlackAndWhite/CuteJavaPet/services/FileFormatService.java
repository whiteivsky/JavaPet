package ru.BlackAndWhite.CuteJavaPet.services;

import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;

import java.util.List;

public interface FileFormatService {
    List<String> upload(MultipartFile[] files);

    FileFormat getIconByFilename(String fileName);

    List<FileFormat> getAllIcons();

    void deleteIconByFilename(String name);
}
