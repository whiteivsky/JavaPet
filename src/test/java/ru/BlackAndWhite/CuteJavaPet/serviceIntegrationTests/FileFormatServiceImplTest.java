package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.common.CreateThings;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.FileFormatServiceImpl;

@Log4j
@PropertySource(value = {"classpath:/uploadStatuses.properties"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class FileFormatServiceImplTest {
    @Mock
    FileFormatDAO fileFormatDAO;
    @Autowired
    @InjectMocks
    private FileFormatServiceImpl fileFormatService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    ;

    @Test
    public void saveOneIcon() throws Exception {
        MultipartFile[] files = CreateThings.newMultipartFileArray(
                new String[]{"txt", "doc", "wrong"},
                new String[]{"png", "png", "jpg"},
                new boolean[]{false, true, false});
        FileFormat[] fileFormats = CreateThings.newFileFormatArray(files);
//        for (FileFormat curFormat:fileFormats             ) {
//            when(fileFormatDAO.save(curFormat)).thenReturn("");
//        }
//
//                .thenReturn(CreateThings.newFileFormat(files[0]));
    }

    @Test
    public void saveManyIcons() {
    }

    @Test
    public void saveNull() {
    }

    @Test
    public void saveEmpty() {
    }

    @Test
    public void getIconByExt() {
    }

    @Test
    public void selectIcons() {
    }

    @Test
    public void deleteIconByFilename() {
    }
}