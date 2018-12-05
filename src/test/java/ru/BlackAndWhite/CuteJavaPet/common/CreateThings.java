package ru.BlackAndWhite.CuteJavaPet.common;

import lombok.extern.log4j.Log4j;
import org.springframework.web.multipart.MultipartFile;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.io.*;
import java.util.stream.IntStream;

@Log4j
public class CreateThings {


    public static MultipartFile[] newMultipartFileArray() throws IOException {
        MultipartFile[] mpArray = new MultipartFile[3];
        // first - isNormal,
        mpArray[0] = newMultipartFile(generateFile("normal","doc",false));
        // second - isEmpty,
        mpArray[1] = newMultipartFile(generateFile("empty","doc",true));
        // third - isWrongExt
        mpArray[2] = newMultipartFile(generateFile("wrong ext","wrg",false));
        return mpArray;
    }

    private static MultipartFile newMultipartFile(File newFile) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return newFile.getAbsolutePath();
            }

            @Override
            public String getOriginalFilename() {
                return newFile.getName();
            }

            @Override
            public String getContentType() {
                return "text/plain";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return newFile.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(newFile);
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
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

    public static Attach newAttach(Integer id) {
        Attach newAttach = newAttach();
        newAttach.setId(id);
        return newAttach;
    }

    public static Attach newAttach() {
        Attach newAttach = new Attach();
        File tmpfile = null;
        try {
            tmpfile = generateFile("newFile","txt",false);
        } catch (IOException e) {
            log.error(e);
        }
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

    private static File generateFile(String filename, String ext, boolean isEmpty) throws IOException {
        final File tempFile = File.createTempFile(filename, ext);
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
        return tempFile;
    }


}
