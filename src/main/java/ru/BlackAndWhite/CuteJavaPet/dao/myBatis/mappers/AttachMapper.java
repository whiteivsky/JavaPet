package ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.BlobInputStreamTypeHandler;
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.LongTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;

import java.util.List;


public interface AttachMapper {

    @Results(id="attachmentMap", value = {
            @Result(property = "id", column = "ID", id = true),
            @Result(property = "fileName", column = "filename", typeHandler = StringTypeHandler.class),
            @Result(property = "fileData", column = "filedata", typeHandler = BlobInputStreamTypeHandler.class),
            @Result(property = "description", column = "description", typeHandler = StringTypeHandler.class),
            @Result(property = "uploadDate", column = "uploaddate", typeHandler = DateTypeHandler.class),
            @Result(property = "size", column = "size", typeHandler = LongTypeHandler.class),
            @Result(property = "mediaType", column = "mediatype", typeHandler = StringTypeHandler.class),
            @Result(property = "owner", column = "owner_id",
                    one = @One(select = "ru.BlackAndWhite.CuteJavaPet.dao.myBatis.mappers.UserMapper.selectUserByID",
                            fetchType = FetchType.LAZY))
    })

    @Select("SELECT * from attachments where owner_id = #{id}")
    List<Attach> selectAttachesByOwnerId(Integer id) throws Exception;

    @Select("SELECT * FROM attachments left join attachments_groups on id=attachments_groups.attach_id where group_id = #{id}")
    @ResultMap("attachmentMap")
    List<Attach> selectAttachesByGroupId(Integer id) throws Exception;

    @Select("SELECT * from attachments where ID = #{id}")
    Attach selectAttachByID(Integer id) throws Exception;

    @Insert("INSERT INTO attachments  VALUES (#{id},  #{fileName}, #{fileData}, #{description}, #{uploadDate}, #{owner.id}, #{size}, #{mediaType})")
    @Options(useGeneratedKeys = true)
    void saveAttach(Attach attach) throws NullPointerException;

    @Insert("INSERT INTO attachments_groups VALUES (#{attach_id}, #{group_id})")
    void addAttachGroup(@Param("attach_id") Integer attach_id, @Param("group_id") Integer group_id) throws Exception;
}
