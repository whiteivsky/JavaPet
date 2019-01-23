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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.common.CreateThings;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.AttachDAO;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.GroupDAO;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.AttachmentServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.FileFormatServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.statuses.UploadStatusesWrapper;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@Log4j
@PropertySource(value = {"classpath:/uploadStatuses.properties"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class AttachmentIntegrationTest {

    @Mock
    AttachDAO attachDAO;
    @Mock
    GroupDAO groupDAO;
    @Mock
    UserService userService;
    @Mock
    FileFormatServiceImpl fileFormatService;
    @Resource
    Environment env;
    @Autowired
    @InjectMocks
    private AttachmentServiceImpl attachmentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @NotNull
    public String[] setUpSaveAttachmentsTest(MultipartFile[] files) throws Exception {
        when(fileFormatService.getIconByFilename(files[0].getOriginalFilename()))
                .thenReturn(CreateThings.newFileFormat(files[0]));
        when(fileFormatService.getIconByFilename(files[1].getOriginalFilename()))
                .thenReturn(CreateThings.newFileFormat(files[1]));
        when(fileFormatService.getIconByFilename(files[2].getOriginalFilename()))
                .thenReturn(null);
        when(groupDAO.selectGroupsByUserId(1)).thenReturn(CreateThings.newGroupList(1));
        when(userService.getCurrentLoggedUser()).thenReturn(CreateThings.newUser(1));

        String[] originalResults = new String[3];
        originalResults[0] = UploadStatusesWrapper.getStatus(UploadStatuses.SUCCESS, files[0].getOriginalFilename());
        originalResults[1] = UploadStatusesWrapper.getStatus(UploadStatuses.EMPTY, files[1].getOriginalFilename());
        originalResults[2] = UploadStatusesWrapper.getStatus(UploadStatuses.WRONG_FORMAT, files[2].getOriginalFilename());
        return originalResults;

    }

    @Test
    public void saveAttachmentsTest() throws IOException {
        MultipartFile[] files = CreateThings.newMultipartFileArray(
                new String[]{"normal", "empty", "wrong"},
                new String[]{"doc", "docx", "wrg"},
                new boolean[]{false, true, false});
        // first - isNormal,
        // second - isEmpty,
        // third - isWrongExt
        try {
            String[] originalResults = setUpSaveAttachmentsTest(files);
            List<String> results = attachmentService.saveAttachments("allFiles", files);
            Assert.assertEquals("result count", files.length, results.size());
            Assert.assertArrayEquals(results.toArray(), originalResults);
        } catch (Exception e) {
            Assert.fail("Error in upload attach" + e.getLocalizedMessage());
        }
    }

    @Test
    public void saveAttachmentsWithError() throws Exception {
        doThrow(Exception.class).when(attachDAO).saveAttach(any());
        doThrow(Exception.class).when(attachDAO).addAttachGroups(any(), any());
        when(fileFormatService.getIconByFilename(anyString())).thenReturn(new FileFormat());

        List<String> results = attachmentService.saveAttachments("", CreateThings.newMultipartFileArray(
                new String[]{"normal"}, new String[]{"txt"}, new boolean[]{false}));
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(UploadStatusesWrapper.getStatus(UploadStatuses.UNKNOW,
                new Exception().getLocalizedMessage()),
                results.get(0));
    }

    @Test
    public void saveAttachmetsNullFilesTest() {
        try {
            Assert.assertNotNull(attachmentService.saveAttachments("testDescr", null));
        } catch (NullPointerException e) {
            Assert.assertEquals(e.getClass().getName(), NullPointerException.class.getName());
        }
    }

    @Test
    public void selectAttachmentByIDTest() throws Exception {
        when(attachDAO.selectAttachByID(1)).thenReturn(CreateThings.newAttach(1));
        Assert.assertEquals(CreateThings.newAttach(1), attachmentService.selectAttachmentByID(1));
    }

    @Test
    public void selectAttachmentsbyUserTest() throws Exception {
        User user = CreateThings.newUser();
        List<Attach> attachList = setUpSelectAttachmentsbyUserTest(user);
        List<Attach> attachListTest = attachmentService.selectAttachmentsbyUser(user);
        Assert.assertNotNull(attachListTest);
        Assert.assertEquals(3, attachListTest.size());
        Assert.assertEquals(attachList, attachListTest);
    }

    @NotNull
    public List<Attach> setUpSelectAttachmentsbyUserTest(User user) throws Exception {
        List<Attach> attachList = new ArrayList<>();
        attachList.add(CreateThings.newAttach());
        attachList.add(CreateThings.newAttach());
        attachList.add(CreateThings.newAttach());
        when(attachDAO.selectAttachesbyUser(user))
                .thenReturn(attachList);
        return attachList;
    }
}