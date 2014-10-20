package it.golem.web.servlet.data;

import it.golem.web.utils.Controller;
import it.golem.web.utils.Navigation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import javax.servlet.ServletException;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class GFSServlet extends Controller {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1429907930016534950L;
	private static final int BUFFER_SIZE = 2 * 1024 * 1024;
	private final GcsService gcsService = GcsServiceFactory
			.createGcsService(RetryParams.getDefaultInstance());
	public static final AppIdentityService APPENGINEID = AppIdentityServiceFactory
			.getAppIdentityService();

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {

		GcsFilename fileName = new GcsFilename(
				APPENGINEID.getDefaultGcsBucketName(),
				asString(nav.getParam("file")));
		GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(
				fileName, 0, BUFFER_SIZE);
		copy(Channels.newInputStream(readChannel), nav.getRes()
				.getOutputStream());

	}

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
			input.close();
			output.close();
		}
	}

}
