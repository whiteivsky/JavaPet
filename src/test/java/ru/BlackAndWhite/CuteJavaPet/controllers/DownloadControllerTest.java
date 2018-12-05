package ru.BlackAndWhite.CuteJavaPet.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ru.BlackAndWhite.CuteJavaPet.services.AttachmentService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;

@RunWith(MockitoJUnitRunner.class)
public class DownloadControllerTest {
    @Mock
    AttachmentService attachmentService;
    @Mock
    UserService userService;
    @InjectMocks
    DownloadController downloadController;

    @Test
    public void showDownloadFileForm() {


    }

    @Test
    public void getFile() {
    }
}