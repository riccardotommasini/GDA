package it.golem.model.users;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity(name = "PendingUser")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4380546696952053944L;
	@Id
	private String confirmCode;
	@Index
	private String email;
	private String name;
	private String surname;
	private String passwordHash;
	private String accessToken;

	public PendingUser(String email, String name, String surname, String pass,
			String code) {
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.passwordHash = pass;
		this.confirmCode = code;
	}

}
