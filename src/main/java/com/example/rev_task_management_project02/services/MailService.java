package com.example.rev_task_management_project02.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper messageHelper=new MimeMessageHelper(message,true,"UTF-8");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(body);

        mailSender.send(message);
    }}