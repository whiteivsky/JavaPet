package ru.BlackAndWhite.CuteJavaPet.model;

import lombok.Data;
import lombok.extern.log4j.Log4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Log4j
@Data
public class Attach {
    private Integer id;
    private String fileName;
    private String description;
    private Date uploadDate;
    private InputStream fileData;
    private String mediaType;
    private User owner;
    private Long size;
    private List<User> accessUsers;
    private List<Group> accessGroups;
    private FileFormat fileFormat;

    public Attach() {
        this.setToday();
    }

    private void setToday() {
        uploadDate = new Date();
    }

    public void setMultiPartFileData(MultipartFile fileData) throws IOException {
        this.size = fileData.getSize();
        this.fileData = fileData.getInputStream();
        this.fileName = fileData.getOriginalFilename();
    }

    public String getFormatSize() {
        boolean si = false;
        int unit = 1024;
        if ( this.size==0) return "0 B";
        if ( this.size < unit) return  this.size + " B";
        int exp = (int) (Math.log (this.size) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + ("i");
        return String.format("%.1f %sB",  this.size / Math.pow(unit, exp), pre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attach attach = (Attach) o;
        return Objects.equals(id, attach.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
