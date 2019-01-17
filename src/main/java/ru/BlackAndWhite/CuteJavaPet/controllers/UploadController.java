package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.FileFormatService;
import ru.BlackAndWhite.CuteJavaPet.services.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Controller

public class UploadController {

    @Autowired
    SecurityService securityService;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    private FileFormatService fileFormatService;


    @GetMapping(path = "upload")
    public Model uploadOneFileHandler(Model model) {

        List<FileFormat> fileFormats = fileFormatService.selectIcons();

        model.addAttribute("formats", fileFormats.stream().map(x -> "." + x.getName())
                .collect(Collectors.joining(",")));
        return model.addAttribute("isActiveUpload", "class=\"active\"");
    }

    @PostMapping(path = "upload")
    public Model handleFileUpload(@RequestParam("filedescription") String filedescription,
                                  @RequestParam("fileData") MultipartFile[] files,
                                  Model curModel) {
        List<FileFormat> fileFormats = fileFormatService.selectIcons();

        curModel.addAttribute("formats", fileFormats.stream().map(x -> "." + x.getName())
                .collect(Collectors.joining(",")));
        return curModel.addAttribute("uploadStatuses",
                attachmentService.saveAttachments(filedescription, files));
    }
}
