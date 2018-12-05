package ru.BlackAndWhite.CuteJavaPet.services.servicesImpl;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.BlackAndWhite.CuteJavaPet.model.Attach;
import ru.BlackAndWhite.CuteJavaPet.services.GroupService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

//import org.springframework.m
@Service
@Log4j
public class EmailServiceImpl {
    @Autowired
    GroupService groupService;

    public static void SendEmailSender(String login, String password, String[] emails, String emailBody) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(25);
        mailSender.setUsername(login);
        mailSender.setPassword(password);
        Properties mailProp = mailSender.getJavaMailProperties();

        mailProp.put("mail.transport.protocol", "smtp");
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.debug", "true");
        // mailProp.put("mail.smtp.starttls.enable", "true");
        mailProp.put("mail.smtp.ssl.enable", "false");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;

        helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(emails);
        helper.setSubject("JavaPet Upload notice");
        helper.setText(emailBody, true);//setting the html page and passing argument true for 'text/html'
//todo executablefeature - вместо потоков
//        try {
//            //Этот метод будет выполняться в побочном потоке
//            Thread myThready = new Thread(() -> {
//                try {
//                    mailSender.send(mimeMessage);
//                } catch (Exception e) {
//                    log.error(e);
//                }
//            });
//            myThready.start();
//        } catch (Exception e) {
//            log.error(e);
//        }
    }

    public static void generateAndSendNotice(String login, String password,
                                             List<Attach> attachList, List<String> recepientsList) {


        //todo подумать как избавиться от пустых инициализаций
        if ((attachList == null) || (attachList.isEmpty())) {
            return;
        }
        String body = "<h2> user '" + attachList.get(0).getOwner().getUserName() + "' uploaded files</h2><br>\n";
        for (Attach curAttach : attachList) {
            if (curAttach != null) {
                body = body + "\n<br>" + curAttach.getFileName() + " (" + curAttach.getSize() + ")";
            }
        }
        String[] toList = new String[recepientsList.size()];
        toList = recepientsList.toArray(toList);

        try {
            SendEmailSender(login,
                    password,
                    toList,
                    body);
        } catch (MessagingException e) {
            log.error(e);
        }
    }
}
