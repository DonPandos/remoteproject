package com.pks;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    public static void sendEmail(Session session, String fromEmail, String toEmail, String subject, String body, ArrayList<String> images){
        try
        {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(fromEmail, "NoReply"));

            msg.setReplyTo(InternetAddress.parse(toEmail, false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            BodyPart messageBodyPart = new MimeBodyPart();
            ((MimeBodyPart) messageBodyPart).setText(body);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            messageBodyPart = new MimeBodyPart();

            for(String filename : images){
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                messageBodyPart.setHeader("Content-ID", filename);
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent("<img src='cid:" + filename + "'>", "text/html");
            }
            msg.setContent(multipart);
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMessagesPop3(final String email, final String password){
        String host = "pop.mail.ru";
        Properties properties = System.getProperties();
        properties.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
        properties.setProperty("mail.pop3.port", "995");
        properties.setProperty("mail.pop3.socketFactory.port", "995");
        Session session = Session.getInstance(properties);
        try {
            Store store = session.getStore("pop3");
            store.connect(host, email, password);
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            for(int i = messages.length - 1; i > messages.length - 6; i --){
                System.out.println("From: " + InternetAddress.toString(messages[i].getFrom()));
                System.out.println("Subject : " + messages[i].getSubject());
                System.out.println("Sent Date : " + messages[i].getSentDate());
                System.out.println("----------------------------------------------------------------");
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void showMessagesImap(final String email, final String password){
        String host = "imap.mail.ru";
        Properties properties = System.getProperties();
        properties.setProperty("mail.imap.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.port", "995");
        properties.setProperty("mail.imap.socketFactory.port", "995");
        Session session = Session.getInstance(properties);
        try {
            Store store = session.getStore("imaps");
            store.connect(host, email, password);
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            for(int i = messages.length - 1; i > messages.length - 6; i --){
                System.out.println("From: " + InternetAddress.toString(messages[i].getFrom()));
                System.out.println("Subject : " + messages[i].getSubject());
                System.out.println("Sent Date : " + messages[i].getSentDate());
                System.out.println("----------------------------------------------------------------");
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
