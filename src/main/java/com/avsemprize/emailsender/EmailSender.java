package com.avsemprize.emailsender;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;


import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class EmailSender {

    private Session session;
    private String emailSender;

    public EmailSender(String emailSender, String credential){
        this.emailSender = emailSender;
        this.session = setEmailSession(this.getGmailProperties(), this.getPasswordAuthentication(emailSender, credential));
    }

    public void sendEmail(MimeMessage message) throws MessagingException {
            Transport.send(message);
    }

    public MimeMessage getEmail(String recipientEmail, String subject, Map<String, Object> params, String template) throws MessagingException, TemplateException, IOException {

        //Session session = this.setEmailSession(this.setEmailProperties(), this.getPasswordAuthentication(this.emailSender, this.password));
        MimeMessage message = new MimeMessage(this.session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setSubject(subject);
            helper.setTo(recipientEmail);
            helper.setFrom(this.emailSender);
            helper.setSentDate(new Date());
            helper.setText(this.getEmailContent(params, template), true);

        return message;
    }

    private String getEmailContent(Map<String, Object> params, String templateName) throws TemplateException, IOException {

            Template template = this.getFreeMarkerConfigurer().getConfiguration().getTemplate(templateName);
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);

    }

    private FreeMarkerConfigurer getFreeMarkerConfigurer(){
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(), "/email-templates");
        configuration.setTemplateLoader(templateLoader);

        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }

    private Session setEmailSession(Properties properties, Authenticator authenticator){
        return Session.getInstance(properties, authenticator);
    }

    private Authenticator getPasswordAuthentication(final String userName, final String password){
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
    }

    private Properties getGmailProperties(){
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "587");
        properties.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS
        return properties;
    }
}
