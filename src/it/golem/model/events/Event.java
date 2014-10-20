package it.golem.model.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	@Id
	private Long id;
	@Index
	private DateTime received;

}
