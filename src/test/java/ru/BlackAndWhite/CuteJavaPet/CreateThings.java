package ru.BlackAndWhite.CuteJavaPet;

import lombok.extern.log4j.Log4j;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.model.Group;
import ru.BlackAndWhite.CuteJavaPet.model.User;

import java.io.*;
import java.util.stream.IntStream;

@Log4j
public class CreateThings {

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
            tmpfile = generateFile();
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

    static File generateFile() throws IOException {
        final File tempFile = File.createTempFile("newAttach", "txt");
        tempFile.deleteOnExit();
        OutputStream is = new FileOutputStream(tempFile);
        IntStream.generate(() -> (int) (Math.random() * 100000))
                .limit(500)
                .forEach(curInt -> {
                    try {
                        is.write(curInt);
                    } catch (IOException e) {
                        log.error(e);
                    }
                });
        is.close();
        return tempFile;
    }
}
