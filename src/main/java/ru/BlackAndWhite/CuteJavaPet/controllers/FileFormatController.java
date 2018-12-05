package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.services.FileFormatService;

@Log4j
@Controller
public class FileFormatController {

    @Autowired
    FileFormatService fileFormatService;

    @GetMapping(path = "uploadIcons")
    public String getViewUploadIcons(ModelMap model) {
        model.addAttribute("isActiveIcons", "class=\"active\"");
        return "uploadIcons";
    }

    @GetMapping(path = "icons")
    public String getViewIcons(ModelMap model) {
        model.addAttribute("icons", fileFormatService.selectIcons());
        model.addAttribute("isActiveIcons", "class=\"active\"");
        return "icons";
    }

    @PostMapping(path = "uploadIcons")
    public String UploadIcons(@RequestParam("icons") MultipartFile[] files,
                              ModelMap curModel) {
        curModel.addAttribute("uploadStatuses",
                fileFormatService.save(files));
        return "uploadIcons";
    }
}
