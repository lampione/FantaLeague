package email;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.sun.mail.smtp.SMTPTransport;

import utilities.Configuration;
import utilities.Utils;

public class SenderEmail {
	public void sendEmail(MimeMessage m, Session s) {

		try {

			Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH);
			String email = config.facadeEmail;
			String passwd = config.facadePassword;
			
			SMTPTransport t = (SMTPTransport) s.getTransport("smtps");
			t.connect("smtp.gmail.com", email, passwd);
			t.sendMessage(m, m.getAllRecipients());
			t.close();

		}
		catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
