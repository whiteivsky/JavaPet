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
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.services.UserService;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.AttachmentServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.FileFormatServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    public MultipartFile[] setUpSaveAttachmentsTest() throws Exception {
        MultipartFile[] files = CreateThings.newMultipartFileArray(
                new String[]{"normal", "empty", "wrong"},
                new String[]{"doc", "docx", "wrg"},
                new boolean[]{false, true, false});
        // first - isNormal,
        // second - isEmpty,
        // third - isWrongExt
        when(fileFormatService.getIconByFilename(files[0].getOriginalFilename()))
                .thenReturn(CreateThings.newFileFormat(files[0]));
        when(fileFormatService.getIconByFilename(files[1].getOriginalFilename()))
                .thenReturn(CreateThings.newFileFormat(files[1]));
        when(fileFormatService.getIconByFilename(files[2].getOriginalFilename()))
                .thenReturn(null);
        when(groupDAO.selectGroupsByUserId(1)).thenReturn(CreateThings.newGroupList(1));
        when(userService.getCurrentLoggedUser()).thenReturn(CreateThings.newUser(1));
        return files;
    }
    @Test
    public void saveAttachmentsTest() {
        try {
            List<String> statuses = attachmentService.saveAttachments("allFiles", setUpSaveAttachmentsTest());
            Assert.assertTrue("error" + env.getProperty("success"),
                    statuses.get(0).startsWith(env.getProperty("success")));
            Assert.assertTrue("error" + env.getProperty("empty"),
                    statuses.get(1).startsWith(env.getProperty("empty")));
            Assert.assertTrue("error" + env.getProperty("wrongFormat"),
                    statuses.get(2).startsWith(env.getProperty("wrongFormat")));
        } catch (Exception e) {
            Assert.fail("Error in upload attach" + e.getLocalizedMessage());
        }
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