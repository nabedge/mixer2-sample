package org.mixer2.sample;

import java.io.IOException;
import java.io.InputStream;

import org.mixer2.Mixer2Engine;
import org.mixer2.jaxb.xhtml.H1;
import org.mixer2.jaxb.xhtml.Html;
import org.mixer2.jaxb.xhtml.P;
import org.mixer2.xhtml.exception.TagTypeUnmatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.mail.*;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	// Server setting
	private static final String HOST_NAME = "localhost";
	private static final int PORT_NUMER = 25;

	private static final String TO_ADDR = "nabedge@example.com",
			TO_NAME = "nabedge", FROM_ADDR = "me@example.com", FROM_NAME = "me",
			SUBJECT = "Sample-title";

	public static void main(String[] args) {
		Main m = new Main();
		try {
			m.sendEmail();
		} catch (EmailException e) {
			logger.warn("Failed to send Email");
			e.printStackTrace();
		}
	}

	private void sendEmail() throws EmailException {
		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName(HOST_NAME);
		email.setSmtpPort(PORT_NUMER);
		email.addTo(TO_ADDR, TO_NAME);
		email.setFrom(FROM_ADDR, FROM_NAME);
		email.setSubject(SUBJECT);

		// Create Html from template
		InputStream is = Main.class.getClassLoader().getResourceAsStream(
				"m2mockup/m2template/mail.html");
		Mixer2Engine m2e = new Mixer2Engine();
		Html html;
		try {
			// Set the html message
			html = m2e.loadHtmlTemplate(is);
			html.getById("headline", H1.class).replaceInner(
					"This is sample to apply mixer2 to E-mail");
			html.getById("content", P.class).replaceInner(
					"Success to send E-mail");
			email.setHtmlMsg(m2e.saveToString(html));
		} catch (IOException e) {
			logger.warn("Mixer2 failed to loadHTMLTemplate");
			e.printStackTrace();
		} catch (TagTypeUnmatchException e) {
			logger.warn("Mixer2 failed to getById");
			e.printStackTrace();
		}

		// Set the alternative message
		email.setTextMsg("Your email client does not support HTML messages");

		// send the email
		email.send();
		logger.debug("Send E-mail");
	}

}
