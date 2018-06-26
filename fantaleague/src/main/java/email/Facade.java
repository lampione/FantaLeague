package email;

import java.util.ArrayList;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import utilities.Configuration;
import utilities.RegistrationUserInvite;
import utilities.Utils;

public interface Facade {

    public static void sendMessageUserNotExists(String[] send, String object, String messageArea,
	    ArrayList<RegistrationUserInvite> emailUserNotExist, String url, String urlFantaLeague, String firstName,
	    String lastName, String leagueName) {

	PropertiesStream propertiesStream = new PropertiesStream();
	ReadObject readObject = new ReadObject();
	ReadMessage readMessage = new ReadMessage();
	SenderEmail senderEmail = new SenderEmail();

	// Get the default Session object.
	Session session = Session.getDefaultInstance(propertiesStream.setPropertiesStream());

	try {

	    Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH);
	    String email = config.facadeEmail;

	    for (String s : send) {

		int i = Utils.contains(s, emailUserNotExist);

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(email));
		ReadSender readSender = new ReadSender();

		readSender.setTo(message, s);

		if (i != -1) { // NOT EXISTS

		    readObject.setObject(message, object);

		    String code = emailUserNotExist.get(i).getCode();

		    String htmlEmail = Utils.getStringFromFile(Utils.HTML_NOT_EXISTS_PATH);

		    htmlEmail = htmlEmail.replaceAll("PH-LINK", urlFantaLeague);
		    htmlEmail = htmlEmail.replaceAll("PH-ADMIN", firstName + " " + lastName);
		    htmlEmail = htmlEmail.replaceAll("PH-LEAGUE", leagueName);
		    htmlEmail = htmlEmail.replaceAll("PH-CODE", code);
		    htmlEmail = htmlEmail.replaceAll("PH-REGISTRATION", url);
		    htmlEmail = htmlEmail.replaceAll("PH-MESSAGE", messageArea);

		    readMessage.setText(message, htmlEmail);
		    senderEmail.sendEmail(message, session);

		} else { // EXISTS

		    readObject.setObject(message, object);

		    String htmlEmail = Utils.getStringFromFile(Utils.HTML_EXISTS_PATH);

		    htmlEmail = htmlEmail.replaceAll("PH-LINK", urlFantaLeague);
		    htmlEmail = htmlEmail.replaceAll("PH-ADMIN", firstName + " " + lastName);
		    htmlEmail = htmlEmail.replaceAll("PH-LEAGUE", leagueName);
		    htmlEmail = htmlEmail.replaceAll("PH-MESSAGE", messageArea);

		    readMessage.setText(message, htmlEmail);
		    senderEmail.sendEmail(message, session);

		}
	    }

	} catch (MessagingException mex) {
	    mex.printStackTrace();
	}
    }

    public static void sendMessage(String[] send, String object, String urlFantaLeague, String url, String code) {

	MimeMessage message;
	PropertiesStream propertiesStream = new PropertiesStream();
	ReadSender readSender = new ReadSender();
	ReadObject readObject = new ReadObject();
	ReadMessage readMessage = new ReadMessage();
	SenderEmail senderEmail = new SenderEmail();

	// Get the default Session object.
	Session session = Session.getDefaultInstance(propertiesStream.setPropertiesStream());

	Configuration config = (Configuration) Utils.getJsonFile(Configuration.class, Utils.CONFIG_PATH);
	String email = config.facadeEmail;

	try {
	    message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(email));

	    for (String s : send) {
		readSender.setTo(message, s);
	    }

	    readObject.setObject(message, object);

	    String htmlEmail = Utils.getStringFromFile(Utils.HTML_FORGOT_PASS_PATH);

	    htmlEmail = htmlEmail.replaceAll("PH-LINK", urlFantaLeague);
	    htmlEmail = htmlEmail.replaceAll("PH-CODE", code);
	    htmlEmail = htmlEmail.replaceAll("PH-RESET", url);

	    readMessage.setText(message, htmlEmail);
	    senderEmail.sendEmail(message, session);

	} catch (MessagingException mex) {
	    mex.printStackTrace();
	}
    }

}
