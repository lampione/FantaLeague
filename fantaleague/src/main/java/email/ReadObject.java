package email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class ReadObject {
	public void setObject(MimeMessage m, String jTx) {
		try {
			m.setSubject(jTx);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
