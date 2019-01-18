package ru.BlackAndWhite.CuteJavaPet.services;

import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;

import java.util.List;

public interface FileFormatService {
    List<String> save(MultipartFile[] files);

    FileFormat getIconByExt(String fileName);

    List<FileFormat> selectIcons();

    void deleteIconByFilename(String name);
}
