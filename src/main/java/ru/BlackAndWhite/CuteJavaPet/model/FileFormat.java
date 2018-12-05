package ru.BlackAndWhite.CuteJavaPet.model;

import lombok.Data;
import lombok.extern.log4j.Log4j;

@Log4j
@Data
public class FileFormat {
    private Integer id;
    private String name;
    private String icon;
    private String mediaType;
    private String path;

    public String getURLIcon() {
        return "data:image/png;base64,"+icon;
    }
}

