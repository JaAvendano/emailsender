package com.avsemprize.emailsender;


import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Map;

public class EmailService {

    private EmailSender emailSender;

    public EmailService(String emailSender, String credential){
        this.emailSender = new EmailSender(emailSender, credential);
    }

    public MimeMessage getEmail(String recipientEmail, String subject, Map<String, Object> params, String template) throws MessagingException, TemplateException, IOException {
        return this.emailSender.getEmail(recipientEmail, subject, params, template);
    }

    public void sendEmail(MimeMessage email) throws MessagingException {
        this.emailSender.sendEmail(email);
    }
}
