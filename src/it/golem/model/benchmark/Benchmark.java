package it.golem.model.benchmark;

import it.golem.model.MCData;
import it.golem.model.Route;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Benchmark {
	@Id
	private Long id;

	@Parent
	protected Ref<Route> route;

	@Index
	protected DateTime creationDate = new DateTime();

	@Index
	private String name;
	@Index
	protected boolean alwaysPlot;
	@Index
	protected boolean active;

	@Override
	public String toString() {
		return name;
	}

	public abstract JSONObject toJson() throws JSONException;

	public abstract MCData toMeasure(Integer hour);

}
