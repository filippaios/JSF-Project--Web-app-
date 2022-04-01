package beans;
import java.util.Properties;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class mail {
	
    private final String username = "savvas.sphy@gmail.com";
    private final String password = "SAVvas#sphy";
	
	@Asynchronous
    public  void send(String to,String subject,String body) {

        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    @Override
					protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("savvas.sphy@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject("Αλλαγη κωδικου πρόσβασης");
            message.setText(body);

            Transport.send(message);

            System.out.println("Επιτυχής Αποστολή");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}