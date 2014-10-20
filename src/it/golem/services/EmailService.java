package it.golem.services;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

	private static final String senderEmail = "tomma156@gmail.com";
	private static final String COMPANY_NAME = "Golem Data Analyser";

	public static void logEmail(String body){
		sendEmail(senderEmail, "GAE LOGGING", body);
	}
	
	
	public static void sendEmail(String email, String subject, String body) {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(senderEmail,
					COMPANY_NAME));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email, "Gentile Utente"));
			msg.setSubject(subject);
			msg.setText(body);
			Transport.send(msg);

		} catch (AddressException e) {
			// TODO notify error
		} catch (MessagingException e) {
			// TODO notify error
		} catch (UnsupportedEncodingException e) {
			// TODO notify error
		}

	}

	public static void sendConfirmationEmail(String email, String confirmCode) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Bravo, ti sei registrato! "
				+ ".\n Completa la tua registrazione cliccando sul seguente link: "
				+ "http://gdanalyser.appspot.com/confirmation?confirmCode="
				+ confirmCode;
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(senderEmail,
					COMPANY_NAME));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email, "Gentile Utente"));
			msg.setSubject("Conferma Registrazione - Golem Data Analyser");
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			// TODO notify error
		} catch (MessagingException e) {
			// TODO notify error
		} catch (UnsupportedEncodingException e) {
			// TODO notify error
		}

	}


	public static void sendPswReset(String email, String resetCode) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Hai richiesto il reset della password, vai a questo link per cambiarla: "
				+ " /confirmation?resetCode="
				+ resetCode;
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(senderEmail,
					"COMPANY_NAME"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email, "Gentile Utente"));
			msg.setSubject("Richiesta modifica password - "+ COMPANY_NAME );
			msg.setText(msgBody);
			Transport.send(msg);

		} catch (AddressException e) {
			// TODO notify error
		} catch (MessagingException e) {
			// TODO notify error
		} catch (UnsupportedEncodingException e) {
			// TODO notify error
		}

	}

}
