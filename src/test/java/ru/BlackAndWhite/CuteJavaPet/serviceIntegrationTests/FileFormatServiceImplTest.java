package ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import ru.BlackAndWhite.CuteJavaPet.statuses.UploadStatusesWrapper;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

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

    @Test
    public void saveManyIcons() throws Exception {
        MultipartFile[] files = getMultipartFiles();
        String[] originalResults = setUpSaveOneIcon(files);

        List<String> results = fileFormatService.upload(files);
        Assert.assertEquals("results count", files.length, results.size());
        Assert.assertArrayEquals(results.toArray(), originalResults);
    }

    public MultipartFile[] getMultipartFiles() throws IOException {
        return CreateThings.newMultipartFileArray(
                new String[]{"txt", "doc", "wrong"},
                new String[]{"png", "png", "jpg"},
                new boolean[]{false, true, false});
    }

    @NotNull
    public String[] setUpSaveOneIcon(MultipartFile[] files) throws Exception {
        FileFormat[] fileFormats = CreateThings.newFileFormatArray(files);
        for (FileFormat curFormat : fileFormats) {
            doNothing().when(fileFormatDAO).save(curFormat);
        }
        String[] originalResults = new String[3];
        originalResults[0] = UploadStatusesWrapper.getStatus(UploadStatuses.SUCCESS, files[0].getOriginalFilename());
        originalResults[1] = UploadStatusesWrapper.getStatus(UploadStatuses.EMPTY, files[1].getOriginalFilename());
        originalResults[2] = UploadStatusesWrapper.getStatus(UploadStatuses.WRONG_FORMAT, files[2].getOriginalFilename());
        return originalResults;
    }

    @Test
    public void saveNull() {
        try {
            Assert.assertNull(fileFormatService.upload(null));
        } catch (Exception e) {
            Assert.fail("null upload");
        }
    }

    @Test
    public void saveWithErrors() throws Exception {

        MultipartFile[] multipartFiles = new MultipartFile[1];
        multipartFiles[0] = CreateThings.newMultipartFile("doc", "png", false);
        doThrow(UnsupportedEncodingException.class)
                .when(fileFormatDAO).save(any());

        List<String> results = fileFormatService.upload(multipartFiles);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(UploadStatusesWrapper.getStatus(UploadStatuses.BAD_ENCODE,
                multipartFiles[0].getOriginalFilename()),
                results.get(0));

        doThrow(Exception.class).when(fileFormatDAO).save(any());
        results = fileFormatService.upload(multipartFiles);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals(UploadStatusesWrapper.getStatus(UploadStatuses.UNKNOW,
                new Exception().getLocalizedMessage()),
                results.get(0));

    }


    @Test
    public void getIconByFilename() throws Exception {
        FileFormat fileFormat = CreateThings.newFileFormat("doc", "png", false);
        when(fileFormatDAO.getIconByFilename("doc")).thenReturn(fileFormat);
        Assert.assertEquals(fileFormatService.getIconByFilename("doc"), fileFormat);
        Assert.assertNotNull(fileFormatService.getIconByFilename("doc"));
    }

    @Test
    public void getAllIcons() throws Exception {
        FileFormat[] fileFormats = CreateThings.newFileFormatArray(getMultipartFiles());
        when(fileFormatDAO.getAllIcons()).thenReturn(Arrays.asList(fileFormats));
        Assert.assertArrayEquals(fileFormatService.getAllIcons().toArray(), fileFormats);
        Assert.assertNotNull(fileFormatService.getAllIcons());
    }

    @Test
    public void deleteIconByFilename() {
        fileFormatService.deleteIconByFilename("doc");
        Mockito.verify(fileFormatDAO).deleteIconByFilename("doc");
        Mockito.verifyNoMoreInteractions(fileFormatDAO);
        Mockito.verifyZeroInteractions(fileFormatDAO);
    }
}