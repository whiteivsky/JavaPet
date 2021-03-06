package ru.BlackAndWhite.CuteJavaPet.controllers;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;

import java.io.File;

@Log4j
@Controller
public class DownloadController {
    @Autowired
    UserService userService;
    @Autowired
    AttachmentService attachmentService;
    @Autowired
    GroupService groupService;

    @GetMapping(path = "download")
    public ModelAndView showDownloadFileForm() {
        try {
            return new ModelAndView("download")
                    .addObject("isActiveDownload", "class=\"active\"")
                    .addObject("attaches",
                            attachmentService.selectAttachmentsbyUser(
                                    userService.getCurrentLoggedUser()));
        } catch (Exception e) {
            log.error(e);
            return new ModelAndView("download")
                    .addObject("isActiveDownload", "class=\"active\"")
                    .addObject("attaches", null)
                    .addObject("errors", e.getLocalizedMessage());
        }
    }

    @GetMapping(path = "download/{file_name}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> getFile(@PathVariable("file_name") String fileName) {
        try {
            Attach attach = attachmentService.selectAttachmentByID(Integer.parseInt(fileName));

            return ResponseEntity
                    .ok()
                    .contentLength(attach.getSize())
                    .contentType(MediaType.parseMediaType(attach.getMediaType()))
                    .body(new InputStreamResource(attach.getFileData()));
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders getAttachHeaders(@PathVariable("file_name") String fileName, Attach attach, File document) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.parseMediaType(attach.getMediaType()));
        header.set(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName.replace(" ", "_"));
        header.setContentLength(document.length());
        header.setContentDispositionFormData("attachment", attach.getFileName());
        return header;
    }
}
