package it.golem.web.servlet.data;

import it.golem.model.events.FileEvent;
import it.golem.resources.FileEventResource;
import it.golem.services.queue.QueueManager;
import it.golem.utils.Parser;
import it.golem.web.enums.PagePaths;
import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(UploadServlet.class
			.getName());
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;

	// private static final GcsService gcsService = GcsServiceFactory
	// .createGcsService(RetryParams.getDefaultInstance());

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		post(nav);

	}

	@Override
	protected void post(Navigation nav) throws IOException, ServletException {
		try {

			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iterator = upload.getItemIterator(nav.getReq());

			while (iterator.hasNext()) {

				FileItemStream item = iterator.next();

				InputStream is = item.openStream();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while ((n = is.read(buf)) >= 0)
					baos.write(buf, 0, n);
				byte[] content = baos.toByteArray();

				InputStream isParsing = new ByteArrayInputStream(content);
				InputStream isSaving = new ByteArrayInputStream(content);

				if (item.isFormField()) {
					log.info("Got a form field: " + item.getFieldName());
				} else {
					log.info("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());
					String fileName = item.getName();

					if (fileName != null && !fileName.isEmpty()
							&& !FileEventResource.exitFile(fileName)) {

						if ("measure_file".equals(item.getFieldName())) {
							handleMeasureFile(item, isParsing, isSaving,
									fileName);
						} else if ("benchmark_file".equals(item.getFieldName())) {
							_log.info("benchmark received");

						}
						nav.redirect(PagePaths.PANEL_SERVLET);
					} else {
						// saveToGS("Duplicate" + fileName, is);
						nav.redirect(PagePaths.ERROR_JSP);
					}
				}

			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handleMeasureFile(FileItemStream item, InputStream isParsing,
			InputStream isSaving, String fileName) throws ParseException,
			IOException {
		FileEvent e = new FileEvent();
		e.setFileName(fileName);
		e.setFileDate(Parser.measureDate(fileName));
		if (item.getName().contains(".csv"))
			e.setLineNumber(QueueManager.queueParsing(isParsing, fileName));

		// List<Measure> measures = MeasureResource.get(e.getFileDate());
		// List<Route> routeOrder = new ArrayList<Route>();
		// List<Ref<Route>> refRouteOrder = new ArrayList<Ref<Route>>();
		// for (Measure measure : measures) {
		// refRouteOrder.add(measure.getRoute());
		// routeOrder.add(measure.getRoute().get());
		// }
		//
		// CacheService.caching("routeOrder", routeOrder);
		//
		// e.setRouteOrder(refRouteOrder);
		FileEventResource.put(e);

	}

	// private void saveToGS(String fileName, InputStream is) throws IOException
	// {
	// GcsFilename f = new GcsFilename(AppIdentityServiceFactory
	// .getAppIdentityService().getDefaultGcsBucketName(), fileName);
	//
	// GcsOutputChannel outputChannel = gcsService.createOrReplace(f,
	// GcsFileOptions.getDefaultInstance());
	//
	// copy(is, Channels.newOutputStream(outputChannel));
	// }
	//
	// private void copy(InputStream input, OutputStream output)
	// throws IOException {
	// try {
	// byte[] buffer = new byte[BUFFER_SIZE];
	// int bytesRead = input.read(buffer);
	// while (bytesRead != -1) {
	// output.write(buffer, 0, bytesRead);
	// bytesRead = input.read(buffer);
	// }
	// } finally {
	// output.close();
	// }
	// }

}
