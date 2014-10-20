package it.golem.model.users;

import it.golem.web.enums.AccessRole;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	private String name;
	private String surname;
	@Index
	private String email;
	private Long numTel;
	private String passwordHash;
	@Index
	private AccessRole role;

	public User(String id, String name, String surname, String email,
			String passwordHash, Long numTel) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.numTel = numTel;
		this.passwordHash = passwordHash;
		role = AccessRole.ADMIN;
	}

	public boolean isAdmin() {
		return AccessRole.ADMIN.equals(role);
	}

}
