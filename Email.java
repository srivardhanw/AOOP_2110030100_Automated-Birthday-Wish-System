
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class Email {

	public static void sendEmail(String from, String password, String to, String occasion, String wish) {
		String host = "smtp.gmail.com"; //hostname here (smtp server)
		Properties properties = System.getProperties();
		
		properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");  // port number for your smtp server
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
		

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(from, password); //your email account password. Generate an app password if you're using gmail and have 
                // two-factor authentication enabled

            }

        });
        try {
	         
	         MimeMessage message = new MimeMessage(session);

	         
	         message.setFrom(new InternetAddress(from));

	         
	         message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

	         
	         message.setSubject(occasion);

	         
	         message.setText(wish);

	         // Send message
	         Transport.send(message);
	         System.out.println("Email sent successfully....");
	      } catch (MessagingException mex) {
	         mex.printStackTrace();
	      }
	}
}
