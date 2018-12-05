package ru.BlackAndWhite.CuteJavaPet.dao.interfaces;

import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;

import java.util.List;

public interface FileFormatDAO {
    void save(FileFormat format);

    FileFormat getIconByFilename(String fileName);

    List<FileFormat> getAllIcons();
}
