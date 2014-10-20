package it.golem.alerting.model;

import it.golem.alerting.enums.AlertLevel;
import it.golem.alerting.enums.AlertStatus;
import it.golem.model.users.User;

import java.util.Date;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotNull;

@Entity
public class Alert implements Comparable<Alert> {

	@Id
	private Long id;

	@Index
	protected String name;
	protected String message;
	@Index
	protected Date sent;
	@Index(IfNotNull.class)
	protected Date read;
	@Index
	protected AlertStatus status;
	@Index
	protected AlertLevel level;
	@Index(IfNotNull.class)
	protected Ref<User> reader;
	
	public Alert() {

	}
	

	public Alert(String name, String message, AlertLevel al) {
		this.name = name;
		this.level = al;
		this.message = message;
		this.status = AlertStatus.PENDING;
		this.sent = new Date(System.currentTimeMillis());
	}

	
	public User getReader(){
		if(reader!=null)
			return reader.safe();
		else
			return null;
	}
	
	public void setReader(Ref<User> u){
		this.reader=u;
	}
	
	public String getName() {
		return name;
	}

	public String getMessage() {
		return message;
	}

	public Date getSent() {
		return sent;
	}

	public Date getRead() {
		return read;
	}

	public AlertStatus getStatus() {
		return status;
	}

	public AlertLevel getLevel() {
		return level;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}

	public void setRead(Date read) {
		this.read = read;
	}

	public void setStatus(AlertStatus status) {
		this.status = status;
	}

	public void setLevel(AlertLevel level) {
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Alert o) {
		return o.getLevel().compareTo(this.level);
	}

}
