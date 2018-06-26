package email;

import java.util.Properties;

public class PropertiesStream {
	public Properties setPropertiesStream() {
		  Properties properties = System.getProperties();
	      properties.setProperty("mail.smtp.port", "25");
	      properties.setProperty("mail.smtp.socketFactory.port", "25");
	      properties.setProperty("smtp.gmail.com", "localhost");
	      return properties;
	}
}
