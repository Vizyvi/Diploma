package main.service;

import main.entity.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {
    @Value("${spring.mail.username}")
    private String username;

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String sendRecoveryPassword(User user) {
        int length = 45;
        String generatedString = RandomStringUtils.random(length, true, true);
        String link = "login/change-password/" + generatedString;
        String body = "<div>\n" +
                "        <h4>Hello, "+ user.getName() + "</h4>\n" +
                "        <p>Click <a href=\"http://localhost:8080/"+ link +"\">here</a> to recover your password</p>\n" +
                "      </div>";
        String subject = "Recovery password";
        try {
            sendEmail(user.getEmail(), body, subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return generatedString;
    }

    private void sendEmail(String emailTo, String body, String subject) throws MessagingException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(emailTo);
        mimeMessageHelper.setFrom(username);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body, true);
        emailSender.send(mimeMessage);
    }
}
