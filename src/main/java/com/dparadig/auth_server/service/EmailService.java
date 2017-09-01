package com.dparadig.auth_server.service;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author jlabarca
 */
@CommonsLog
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public boolean sendMail(String toEmail, String subject, String message) {
        try {
            MimeMessage mMessage = javaMailSender.createMimeMessage();
            mMessage.setFrom(new InternetAddress("Entel Secure Cloud <registroweb_entelsc@entel.cl>"));
            MimeMessageHelper helper = new MimeMessageHelper(mMessage);
            helper.setFrom(new InternetAddress("Entel Secure Cloud <registroweb_entelsc@entel.cl>"));
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText("<html><body>" + message + "</body></html>", true);
            javaMailSender.send(mMessage);
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }
}