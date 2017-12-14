package com.dparadig.auth_server.service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.apachecommons.CommonsLog;

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

    public boolean sendMail(String toEmail, String subject, String message, InternetAddress from) {
        try {
            MimeMessage mMessage = javaMailSender.createMimeMessage();
            mMessage.setFrom(from);
            MimeMessageHelper helper = new MimeMessageHelper(mMessage);
            helper.setFrom(from);
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

    public boolean sendConfirmationEmail(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Activate your Account";
            String message = "Confirm your new registered email address with this button: <br> <a href='"+url+"'>Confirm</a>";
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendPassResetEmail(String toEmail, String url){
        try {
            InternetAddress from = new InternetAddress("dParadig <sgi@dparadig.com>");
            String subject = "Reset Your Password";
            String message = "Reset Your Password with this button: <br> <a href='"+url+"'>Reset</a>";
            return sendMail(toEmail,subject,message,from);
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        }
    }
}