package email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class ReadMessage {

	public void setText(MimeMessage m, String jTx) {
		try {
			m.setContent(jTx, "text/html; charset=utf-8");
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
