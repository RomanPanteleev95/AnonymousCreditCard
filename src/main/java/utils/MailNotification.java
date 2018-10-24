package utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
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

        Transport tr = mailSession.getTransport();
        tr.connect("anoncreditcard@gmail.com", "Gthtcnhjqrf32");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }
}
