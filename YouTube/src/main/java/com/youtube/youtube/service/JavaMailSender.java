package com.youtube.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class JavaMailSender {
    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;
    public void sendEmail(String toMail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("yani.v.yakimov@gmail.com");
        message.setTo(toMail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
    }
}
