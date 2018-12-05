package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.SecurityService;

@Log4j
@Controller

public class UploadController {

    @Autowired
    SecurityService securityService;
    @Autowired
    AttachmentService attachmentService;


    @GetMapping(path = "upload")
    public Model uploadOneFileHandler(Model model) {
        return model.addAttribute("isActiveUpload", "class=\"active\"");
    }

    @PostMapping(path = "upload")
    public Model handleFileUpload(@RequestParam("filedescription") String filedescription,
                                  @RequestParam("fileData") MultipartFile[] files,
                                  Model curModel) {
        return curModel.addAttribute("uploadStatuses",
                attachmentService.saveAttachments(filedescription, files));
    }
}
