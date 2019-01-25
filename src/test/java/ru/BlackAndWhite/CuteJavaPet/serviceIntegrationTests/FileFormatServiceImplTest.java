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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.common.ServiceTestConfig;
import ru.BlackAndWhite.CuteJavaPet.dao.interfaces.FileFormatDAO;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.services.servicesImpl.FileFormatServiceImpl;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ru.BlackAndWhite.CuteJavaPet.common.CreateThings.*;

@Log4j
//@PropertySource(value = {"classpath:/uploadStatuses.properties"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ServiceTestConfig.class})
public class FileFormatServiceImplTest {
    @Mock
    FileFormatDAO fileFormatDAO;
    @Autowired
    @InjectMocks
    private FileFormatServiceImpl fileFormatService;

    private MultipartFile fileSuccess = getFileByStatus(UploadStatuses.SUCCESS);
    private MultipartFile fileEmpty = getFileByStatus(UploadStatuses.EMPTY);
    private MultipartFile fileWrongFormat = getFileByStatus(UploadStatuses.WRONG_FORMAT);
    private Map<UploadStatuses, MultipartFile> allTypesFilesMap = new HashMap<>();

    {
        allTypesFilesMap.put(UploadStatuses.SUCCESS, fileSuccess);
        allTypesFilesMap.put(UploadStatuses.EMPTY, fileEmpty);
        allTypesFilesMap.put(UploadStatuses.WRONG_FORMAT, fileWrongFormat);
    }


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        doNothing().when(fileFormatDAO).save(any(FileFormat.class));
    }

    @Test
    public void upload_ManyIcons() throws Exception {
        MultipartFile[] files = getFiles(allTypesFilesMap);
        String[] originalResults = getStatus(allTypesFilesMap);

        List<String> results = fileFormatService.upload(files);

        assertEquals("results count", files.length, results.size());
        assertArrayEquals(results.toArray(), originalResults);
    }


    @Test
    public void upload_Null() {
        assertNull(fileFormatService.upload(null));
    }

    @Test
    public void upload_UnsupportedEncodingException() throws Exception {
        MultipartFile[] multipartFiles = getFiles(allTypesFilesMap);
        String[] originalResults = getStatus(allTypesFilesMap);
        doThrow(UnsupportedEncodingException.class).when(fileFormatDAO).save(newFileFormat(fileWrongFormat));

        List<String> results = fileFormatService.upload(multipartFiles);

        assertEquals(originalResults[originalResults.length - 1], results.get(results.size() - 1));
    }

    @Test
    public void upload_Exception() throws Exception {
        MultipartFile[] multipartFiles = getFiles(allTypesFilesMap);
        String[] originalResults = getStatus(allTypesFilesMap);
        doThrow(Exception.class).when(fileFormatDAO).save(newFileFormat(fileSuccess));

        List<String> results = fileFormatService.upload(multipartFiles);

        assertArrayEquals(originalResults, results.toArray());
    }


    @Test
    public void getIconByFilename() throws Exception {
        FileFormat fileFormat = newFileFormat("doc", "png", false);
        when(fileFormatDAO.getIconByFilename(anyString())).thenReturn(fileFormat);

        assertEquals(fileFormat, fileFormatService.getIconByFilename("doc"));
        verify(fileFormatDAO).getIconByFilename("doc");
    }

    @Test
    public void getAllIcons() throws Exception {
        FileFormat[] fileFormats = newFileFormatArray(getFiles(allTypesFilesMap));
        when(fileFormatDAO.getAllIcons()).thenReturn(Arrays.asList(fileFormats));

        assertArrayEquals(fileFormatService.getAllIcons().toArray(), fileFormats);
    }

    @Test
    public void deleteIconByFilename() {
        fileFormatService.deleteIconByFilename("doc");

        verify(fileFormatDAO).deleteIconByFilename("doc");
        verifyNoMoreInteractions(fileFormatDAO);
    }
}