package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers;

import org.apache.ibatis.annotations.*;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;

import java.util.List;

public interface FileFormatsMapper {

    @Insert("INSERT INTO fileformats VALUES (#{id},#{name},#{icon})")
    @Options(useGeneratedKeys = true)
    void save(FileFormat format);

    @Delete("DELETE FROM fileformats WHERE name=#{name}")
    void deleteIconByFilename(@Param("name") String name);

    @Select("SELECT * FROM fileformats ORDER BY name ASC")
    List<FileFormat> getAllIcons();

    @Select("SELECT * FROM fileformats WHERE name = #{name}")
    FileFormat getIconByExt(String name);
}
