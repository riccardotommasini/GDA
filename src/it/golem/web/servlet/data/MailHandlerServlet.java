package it.golem.web.servlet.data;

import it.golem.alarm.SimpleAlarm;
import it.golem.model.events.FileEvent;
import it.golem.resources.FileEventResource;
import it.golem.services.EmailService;
import it.golem.services.queue.QueueManager;
import it.golem.utils.Parser;
import it.golem.web.exceptions.DuplicateEmailException;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class MailHandlerServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	// private final GcsService gcsService = GcsServiceFactory
	// .createGcsService(RetryParams.getDefaultInstance());
	public static final Logger _log = Logger.getLogger(MailHandlerServlet.class
			.getName());

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session, nav.getReq()
					.getInputStream());

			TimeZone zone = TimeZone.getTimeZone("GMT+1");
			Calendar cal = Calendar.getInstance(zone);
			cal.setTime(message.getSentDate());
			Date sentDate = cal.getTime();

			String subject = message.getSubject();
			String address = message.getFrom()[0].toString();
			String contentType = message.getContentType();

			_log.info("Got an email. At: " + sentDate + " Subject = " + subject
					+ " from " + address + " with Content Type : "
					+ contentType);

			nav.setContentType("text/plain");
			PrintWriter out = nav.getWriter();
			out.print("Email Received");

			// if (Alarm.emailLimit()) {
			handleAttachment(Arrays.asList(message));
			// }

		} catch (MessagingException ex) {
			_log.log(Level.WARNING,
					"Failure in receiving email : " + ex.getMessage());
			EmailService.logEmail(ex.getMessage());
			ex.printStackTrace();
		} catch (IOException ex) {
			_log.log(Level.WARNING,
					"Failure in receiving email : " + ex.getMessage());
			EmailService.logEmail(ex.getMessage());
			ex.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (DuplicateEmailException e) {
			SimpleAlarm.duplicateEmail(e.getMessage());
			e.printStackTrace();

		}
	}

	private void handleAttachment(List<MimeMessage> temp)
			throws MessagingException, IOException, ParseException,
			DuplicateEmailException {
		for (MimeMessage message : temp) {

			FileEvent e = new FileEvent();

			String address = message.getFrom()[0].toString();

			e.setReceived(new DateTime());
			e.setSender(address);

			Multipart multipart = (Multipart) message.getContent();
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (!Part.ATTACHMENT
						.equalsIgnoreCase(bodyPart.getDisposition())
						&& StringUtils.isEmpty(bodyPart.getFileName())) {
					continue; // dealing with attachments only
				}
				String fileName = bodyPart.getFileName();
				InputStream is = bodyPart.getInputStream();

				if (!FileEventResource.exitFile(fileName)) {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int n = 0;
					while ((n = is.read(buf)) >= 0)
						baos.write(buf, 0, n);
					byte[] content = baos.toByteArray();

					InputStream isParsing = new ByteArrayInputStream(content);
					InputStream isSaving = new ByteArrayInputStream(content);

					e.setFileName(fileName);
					e.setFileDate(Parser.measureDate(fileName));
					// saveToGS(fileName, isSaving);

					if (fileName.contains(".csv")) {
						e.setLineNumber(QueueManager.queueParsing(isParsing,
								fileName));

					} else if (fileName.contains(".xls")) {
						// e.setLineNumber(Parser.parseXLS(isParsing,
						// fileName));
					}

					FileEventResource.put(e);

				} else {
					// saveToGS("Duplicate" + fileName, is);
					throw new DuplicateEmailException(fileName);
				}
			}

		}
	}

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		post(nav);
	}

	//
	// private void saveToGS(String fileName, InputStream is) throws IOException
	// {
	// GcsOutputChannel outputChannel = gcsService.createOrReplace(
	// new GcsFilename(AppIdentityServiceFactory
	// .getAppIdentityService().getDefaultGcsBucketName(),
	// fileName), GcsFileOptions.getDefaultInstance());
	//
	// copy(is, Channels.newOutputStream(outputChannel));
	// }

	private void copy(InputStream input, OutputStream output)
			throws IOException {
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = input.read(buffer);
			while (bytesRead != -1) {
				output.write(buffer, 0, bytesRead);
				bytesRead = input.read(buffer);
			}
		} finally {
			output.close();
		}
	}

}
