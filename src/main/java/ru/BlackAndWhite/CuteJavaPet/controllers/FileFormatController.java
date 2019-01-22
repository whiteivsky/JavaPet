package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.BlackAndWhite.CuteJavaPet.services.FileFormatService;

@Log4j
@Controller
public class FileFormatController {

    @Autowired
    FileFormatService fileFormatService;

    @RequestMapping(path = {"admin", "uploadIcons"})
    public ModelAndView getViewIcons(ModelAndView model) {
        model.addObject("icons", fileFormatService.getAllIcons());
        model.addObject("isActiveAdmin", "class=\"active\"");
        model.setViewName("admin");
        return model;
    }

    @PostMapping(path = "uploadIcons")
    public ModelAndView UploadIcons(@RequestParam("newIcons") MultipartFile[] files,
                                    ModelMap model) {
        model.addAttribute("uploadStatuses", fileFormatService.upload(files));
        return new ModelAndView("forward:/admin", model);
    }

    @PostMapping(path = "deleteIcons")
    public ModelAndView UploadIcons(@RequestParam("iconForDelete") String iconForDelete,
                                    ModelAndView model) {
        fileFormatService.deleteIconByFilename(iconForDelete);
        model.setViewName("redirect:/admin");
        return model;
    }
}
