package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.common.CreateThings;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.AttachDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.AttachmentServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.FileFormatServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Log4j

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class AttachmentIntegrationTest {

    @Mock
    AttachDAO attachDAO;
    @Mock
    GroupService groupService;
    @Mock
    UserService userService;
    @Mock
    FileFormatServiceImpl fileFormatService;

    @Autowired
    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    private MultipartFile fileSuccess = CreateThings.getFileByStatus(UploadStatuses.SUCCESS);
    private MultipartFile fileEmpty = CreateThings.getFileByStatus(UploadStatuses.EMPTY);
    private MultipartFile fileWrongFormat = CreateThings.getFileByStatus(UploadStatuses.WRONG_FORMAT);
    private Map<UploadStatuses, MultipartFile> allTypesFilesMap = new HashMap<>();

    {
        allTypesFilesMap.put(UploadStatuses.SUCCESS, fileSuccess);
        allTypesFilesMap.put(UploadStatuses.EMPTY, fileEmpty);
        allTypesFilesMap.put(UploadStatuses.WRONG_FORMAT, fileWrongFormat);
    }


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(fileFormatService.getIconByFilename(fileSuccess.getOriginalFilename())).thenReturn(new FileFormat());
        when(fileFormatService.getIconByFilename(fileWrongFormat.getOriginalFilename())).thenReturn(null);
        when(groupService.selectGroupsByUserId(1)).thenReturn(CreateThings.newGroupList(1));
        when(userService.getCurrentLoggedUser()).thenReturn(CreateThings.newUser(1));
        when(attachDAO.selectAttachByID(1)).thenReturn(CreateThings.newAttach(1));
    }

    @Test
    public void saveAttachmentsTest() {
        MultipartFile[] files = CreateThings.getFiles(allTypesFilesMap);
        String[] actualStatus = CreateThings.getStatus(allTypesFilesMap);

        List<String> resultStatus = attachmentService.saveAttachments("allFiles", files);

        Assert.assertArrayEquals(resultStatus.toArray(), actualStatus);
    }

    @Test
    public void saveAttachmentsWithError() throws Exception {
        doThrow(Exception.class).when(attachDAO).saveAttach(any());
        doThrow(Exception.class).when(attachDAO).addAttachGroups(any(), any());

        List<String> results = attachmentService.saveAttachments("", new MultipartFile[]{fileSuccess});

        assertEquals(UploadStatuses.UNKNOW.getStatus(new Exception().getLocalizedMessage()), results.get(0));
    }

    @Test
    public void saveAttachmetsNullFilesTest() {
        try {
            attachmentService.saveAttachments("testDescr", null);
        } catch (NullPointerException e) {
            assertEquals(e.getClass().getName(), NullPointerException.class.getName());
        }
    }

    @Test
    public void selectAttachmentByIDTest() throws Exception {
//        when(attachDAO.selectAttachByID(anyInt())).thenReturn(CreateThings.newAttach(1));
        assertEquals(CreateThings.newAttach(1), attachmentService.selectAttachmentByID(1));
    }

    @Test
    public void selectAttachmentsbyUserTest() throws Exception {
        User user = CreateThings.newUser();
        List<Attach> attachList = setUpSelectAttachmentsbyUserTest(user);

        List<Attach> attachListTest = attachmentService.selectAttachmentsbyUser(user);

        assertNotNull(attachListTest);
        assertEquals(3, attachListTest.size());
        assertEquals(attachList, attachListTest);
    }

    @NotNull
    public List<Attach> setUpSelectAttachmentsbyUserTest(User user) throws Exception {
        List<Attach> attachList = new ArrayList<>();
        attachList.add(CreateThings.newAttach());
        attachList.add(CreateThings.newAttach());
        attachList.add(CreateThings.newAttach());
        when(attachDAO.selectAttachesbyUser(user)).thenReturn(attachList);
        return attachList;
    }


}