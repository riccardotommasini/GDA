package it.golem.model.users;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name = "PendingToken")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5364875833890078554L;
	@Id
	private String resetCode;
	@Index
	private String email;

}
