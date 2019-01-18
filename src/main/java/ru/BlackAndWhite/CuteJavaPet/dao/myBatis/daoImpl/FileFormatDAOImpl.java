package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.daoImpl;

import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.FileFormatsMapper;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;

import java.util.List;

@Log4j
@Repository
public class FileFormatDAOImpl implements FileFormatDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void save(FileFormat format) {
        sqlSession.getMapper(FileFormatsMapper.class).save(format);
        sqlSession.commit();
    }

    @Override
    public List<FileFormat> getAllIcons() {
        List<FileFormat> fileFormats = sqlSession.getMapper(FileFormatsMapper.class).getAllIcons();
        sqlSession.commit();
        return fileFormats;
    }

    public FileFormat getIconByFilename(String fileName) {
        String ext;
        if (fileName.contains(".")) {
            try {
                ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            } catch (Exception e) {
                ext = fileName;
            }
        } else {
            ext = fileName;
        }

        FileFormat fileFormat = sqlSession.getMapper(FileFormatsMapper.class).getIconByExt(ext);
        sqlSession.commit();
        return fileFormat;
    }

    public void deleteIconByFilename(String name) {
        sqlSession.getMapper(FileFormatsMapper.class).deleteIconByFilename(name);
        sqlSession.commit();
    }
}
