package utils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.Properties;

public class MailNotification {
    public static void sendPassword(String email, String messagePassword) throws MessagingException, IOException {
        final Properties properties = new Properties();
        properties.load(MailNotification.class.getClassLoader().getResourceAsStream("mail.properties"));
        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("anoncreditcard@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Password");
        message.setText("Ваш пароль для личного кабинета: " + messagePassword);
//
//        PrintWriter printWriter = new PrintWriter("password.txt");
//        printWriter.write(messagePassword);
//        printWriter.close();

//        MimeBodyPart messageBodyPart = new MimeBodyPart();
//        Multipart multipart = new MimeMultipart();
//        String file = "password.txt";
//        String fileName = "password.txt";
//        DataSource source = new FileDataSource(file);
//        messageBodyPart.setDataHandler(new DataHandler(source));
//        messageBodyPart.setFileName(fileName);
//        multipart.addBodyPart(messageBodyPart);
//        message.setContent(multipart);


        Transport tr = mailSession.getTransport();
        tr.connect("anoncreditcard@gmail.com", "Gthtcnhjqrf32");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
