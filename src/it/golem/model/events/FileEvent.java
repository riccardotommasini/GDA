package it.golem.model.events;

import it.golem.alerting.model.Alert;
import it.golem.model.Route;
import it.golem.resources.FileEventResource;
import it.golem.resources.RouteResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.google.appengine.api.blobstore.BlobKey;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Subclass;
import com.googlecode.objectify.condition.IfNotEmpty;

@Subclass(index = true)
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class FileEvent extends Event implements Serializable

{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8101109052177075945L;

	private BlobKey file;

	@Index
	private DateTime fileDate;
	@Index
	private String fileName;
	@Index
	private String sender;
	private int lineNumber;

	@Index({ IfNotEmpty.class })
	private List<Ref<Alert>> alerts = new ArrayList<Ref<Alert>>();
	@Index({ IfNotEmpty.class })
	private List<Ref<Route>> routeOrder = new ArrayList<Ref<Route>>();

	public void update() {
		if (routeOrder.isEmpty() || routeOrder.size() == 0) {
			for (Route r : RouteResource.get(fileDate)) {
				routeOrder.add(Ref.create(r));
			}
			FileEventResource.put(this);
		}
	}

	public int getDay() {

		return fileDate.getDayOfWeek();
	}

}
