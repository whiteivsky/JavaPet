package ru.BlackAndWhite.CuteJavaPet.common;

import lombok.extern.log4j.Log4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.FileFormat;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;
import ru.BlackAndWhite.CuteJavaPet.serviceIntegrationTests.AttachmentIntegrationTest;
import ru.BlackAndWhite.CuteJavaPet.statuses.enums.UploadStatuses;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@Log4j
public class CreateThings {


    public static MultipartFile[] newMultipartFileArray(String[] name, String[] ext, boolean[] empty) throws IOException {
        MultipartFile[] mpArray = new MultipartFile[name.length];
        for (int i = 0; i < name.length; i++) {
            mpArray[i] = newMultipartFile(name[i], ext[i], empty[i]);
        }
        return mpArray;
    }

    public static MultipartFile newMultipartFile(String name, String ext, boolean empty) {
        return newMultipartFile(generateFile(name, ext, empty));
    }

    private static MultipartFile newMultipartFile(File newFile) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return newFile.getAbsolutePath();
            }

            @Override
            public String getOriginalFilename() {return newFile.getName();}

            @Override
            public String getContentType() {
                return "text/plain";
            }

            @Override
            public boolean isEmpty() {
                return newFile.length() == 0;
            }

            @Override
            public long getSize() {
                return newFile.length();
            }

            @Override
            public byte[] getBytes() throws IOException {return new byte[0];}

            @Override
            public InputStream getInputStream() throws IOException {return new FileInputStream(newFile);}

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
    }

    public static List<User> newUserList(int count) {
        return IntStream.range(0, count).mapToObj(CreateThings::newUser).collect(Collectors.toList());
    }

    public static User newUser(Integer id) {
        User newUser = newUser();
        newUser.setId(id);
        return newUser;
    }

    public static User newUser() {
        User newUser = new User();
        newUser.setUserName("testName");
        newUser.setEmail("testName@javapet.ru");
        newUser.setPassword("testName");
        return newUser;
    }

    public static Group newGroup(Integer id) {
        Group newGroup = newGroup();
        newGroup.setId(id);
        return newGroup;
    }

    public static Group newGroup() {
        Group newGroup = new Group();
        newGroup.setGroupName("testGroup");
        return newGroup;
    }

    public static List<Group> newGroupList(int listSize) {
        List<Group> groups = IntStream.range(0, listSize)
                .mapToObj(i -> CreateThings.newGroup()).collect(Collectors.toList());
        return groups;
    }

    public static Attach newAttach(Integer id) {
        Attach newAttach = newAttach();
        newAttach.setId(id);
        return newAttach;
    }

    public static Attach newAttach() {
        Attach newAttach = new Attach();
        File tmpfile = generateFile("newFile", "txt", false);
        InputStream is = null;
        try {
            is = new FileInputStream(tmpfile);
        } catch (FileNotFoundException e) {
            log.error(e);
        }

        newAttach.setMediaType("plain/text");
        newAttach.setFileData(is);
        newAttach.setFileName("newAttach.txt");
        newAttach.setSize(tmpfile.length());
        User tmpUser = new User();
        tmpUser.setId(1);
        newAttach.setOwner(tmpUser);
        newAttach.setDescription("Temporary description");

        //newAttach.setFileFormat(fileFormatDAO.getIconByFilename("newAttach.txt"));
        return newAttach;
    }

    public static FileFormat[] newFileFormatArray(MultipartFile[] fileDatas) throws Exception {
        FileFormat[] newResult = new FileFormat[fileDatas.length];
        for (int i = 0; i < fileDatas.length; i++) {
            newResult[i] = newFileFormat(fileDatas[i]);
        }
        return newResult;
    }

    public static FileFormat newFileFormat(MultipartFile fileData) throws Exception {
        FileFormat format = new FileFormat();
//        byte[] encodeBase64 = Base64.encodeBase64(fileData.getBytes());
//        String base64Encoded = null;
//        try {
//            base64Encoded = new String(encodeBase64, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        format.setIcon(base64Encoded);
//        format.setMediaType(fileData.getContentType());
////        log.info("setName = " + fileData.getName().substring(fileData.getName().lastIndexOf(".")+1));
//        format.setName(fileData.getName().substring(fileData.getName().indexOf(".")));
        return format;
    }

    public static FileFormat newFileFormat(String filename, String ext, boolean isEmpty) throws Exception {
        MultipartFile fileData = newMultipartFile(generateFile(filename, ext, isEmpty));
        return newFileFormat(fileData);
    }

    private static File generateFile(String filename, String ext, boolean isEmpty) {
        File tempFile;
        try {
            tempFile = File.createTempFile(filename, "." + ext);
            tempFile.deleteOnExit();
            OutputStream is = new FileOutputStream(tempFile);
            if (!isEmpty) {
                IntStream.generate(() -> (int) (Math.random() * 100000))
                        .limit(500)
                        .forEach(curInt -> {
                            try {
                                is.write(curInt);
                            } catch (IOException e) {
                                log.error(e);
                            }
                        });
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            tempFile = null;
        }
        return tempFile;
    }

    public static MultipartFile getFileByStatus(UploadStatuses status) {
        switch (status) {
            case EMPTY:
                return newMultipartFile("Empty", "empty", true);
            case WRONG_FORMAT:
                return newMultipartFile("WrongFormat", "wrongFormat", false);
            case SUCCESS:
                return newMultipartFile("Success", "success", false);
            case BAD_ENCODE:
                break;
            case UNKNOW:
                break;
        }
        return null;
    }

    public static MultipartFile[] getFiles(Map<UploadStatuses, MultipartFile> allTypesFilesMap) {
        return allTypesFilesMap.values().toArray(new MultipartFile[]{});
    }

    public static String[] getStatus(Map<UploadStatuses, MultipartFile> allTypesFilesMap) {
        return allTypesFilesMap.keySet().stream().map(x -> x.getStatus(allTypesFilesMap.get(x).getOriginalFilename())).toArray(String[]::new);
    }

//
//
//    public static Map<MultipartFile, String> newMultipartFileArray2(UploadStatuses[] uploadStatuses) {
//        MultipartFile[] files = CreateThings.newMultipartFileArray(uploadStatuses);
////                new String[]{"normal", "empty", "wrong"},
////                new String[]{"doc", "docx", "wrg"},
////                new boolean[]{false, true, false});
//
//
//
//        String[] originalResults = new String[3];
//        originalResults[0] = UploadStatuses.SUCCESS.getStatus(files[0].getOriginalFilename());
//        originalResults[1] = UploadStatuses.EMPTY.getStatus(files[1].getOriginalFilename());
//        originalResults[2] = UploadStatuses.WRONG_FORMAT.getStatus(files[2].getOriginalFilename());
//
//
//
//        AttachmentIntegrationTest.StartingData startingData = new AttachmentIntegrationTest.StartingData();
//        startingData.setFiles(files);
//        startingData.setFilesUploadStatus(originalResults);
//
//        return
//    }


}
