package com.pks;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    static Scanner scn = new Scanner(System.in);
    static Session session;

    public static void main(String[] args) {
        final String email, password;
        System.out.println("Enter email: ");
//        email = scn.nextLine();
        email = "bogdant99@mail.ru";
        System.out.println("Enter password: ");
//        password = scn.nextLine();
        password = "tipek440";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.mail.ru"); //SMTP Host
        props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
        props.put("mail.smtp.port", "465"); //SMTP Port

        Authenticator auth = new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        };
        session = Session.getDefaultInstance(props, auth);


        int key;
        do {
            System.out.println("1 - Send email.");
            System.out.println("2 - View last 5 messages(pop3).");
            System.out.println("3 - View last 5 messages(imap).");
            System.out.println("0 - Exit");
            key = Integer.parseInt(scn.nextLine());
            switch (key){
                case 1:
                    sendMsgMenu(email);
                    break;
                case 2:
                    EmailUtils.showMessagesPop3(email, password);
                    break;
                case 3:
                    EmailUtils.showMessagesImap(email, password);
                    break;
                case 0:
                    System.out.println("Exit...");
                    break;
                default:
                    System.out.println("Choose one from list.");
                    break;
            }
        } while (key != 0);
    }

    static void sendMsgMenu(String email){
        String subject, body, toEmail;
        System.out.println("Enter reciever: ");
        toEmail = scn.nextLine();
        System.out.println("Enter subject of message: ");
        subject = scn.nextLine();
        System.out.println("Enter message: ");
        body = scn.nextLine();
        int imgKey;
        ArrayList<String> images = new ArrayList();
        do {
            System.out.println("Do you want to add image? (1/0): ");
            imgKey = Integer.parseInt(scn.nextLine());
            switch (imgKey){
                case 1:
                    System.out.println("Enter image path: ");
                    String imgPath = scn.nextLine();
                    if(!new File(imgPath).exists()) System.out.println("File dont found.");
                    else {
                        images.add(imgPath);
                        System.out.println("File added.");
                    }
                    break;
                case 0:
                    break;
                default:
                     System.out.println("Choose one from list.");
                     break;
            }
        } while(imgKey != 0);
        EmailUtils.sendEmail(session, email, toEmail,subject, body, images);

    }
}
