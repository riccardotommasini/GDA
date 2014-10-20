package it.golem.resources;

import it.golem.datastore.OfyService;
import it.golem.model.users.PendingToken;
import it.golem.model.users.PendingUser;
import it.golem.model.users.User;
import it.golem.services.EmailService;
import it.golem.web.enums.AccessRole;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

import com.googlecode.objectify.NotFoundException;

public class AuthenticationResource {

	private static final Class<PendingToken> tokenClazz = PendingToken.class;
	private static final Class<PendingUser> clazz = PendingUser.class;
	private static final String ALGORITHM = "SHA-1";
	private static final String ENCODING = "UTF-8";
	private static final int CODE_LENGTH = 42;

	public static String hash(String token) {
		try {
			MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
			digest.update(token.getBytes(ENCODING));
			return Hex.encodeHexString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PendingUser createPending(String name, String surname,
			String email, String passHash) {

		if (!(email.equals("")) && !(name.equals("")) && !(surname.equals(""))
				&& !passHash.equals("") && !(passHash == null)) {

			String confirmCode = RandomStringUtils
					.randomAlphanumeric(CODE_LENGTH);

			PendingUser pending = new PendingUser();
			pending.setName(name);
			pending.setSurname(surname);
			pending.setEmail(email);
			pending.setPasswordHash(passHash);
			pending.setConfirmCode(confirmCode);

			EmailService.sendConfirmationEmail(email, confirmCode);

			return OfyService.get(OfyService.saveSync(pending));

		} else
			return null;
		// TODO exception
	}

	public static User completeRegistration(String confirmCode)
			throws NotFoundException {
		PendingUser pending = OfyService.get(clazz, confirmCode);

		User user = new User();
		user.setId(pending.getName() + "." + pending.getSurname()
				+ Math.abs((new Random()).nextInt()));
		user.setName(pending.getName());
		user.setSurname(pending.getSurname());
		user.setEmail(pending.getEmail());
		user.setPasswordHash(pending.getPasswordHash());
		user.setRole(AccessRole.USER);

		if (user.getEmail().equals("tomma156@gmail.com")) {
			user.setRole(AccessRole.ADMIN);
		}
		OfyService.delete(pending);

		return UserResource.put(user);
	}

	public static PendingUser getPending(String confirmCode)
			throws NotFoundException {
		return (OfyService.get(clazz, confirmCode));

	}

	public static PendingToken requestPswReset(String email)
			throws com.googlecode.objectify.NotFoundException,
			NotFoundException {

		User u = UserResource.getByEmail(email);
		String resetCode = RandomStringUtils.randomAlphanumeric(CODE_LENGTH);
		PendingToken token = new PendingToken(resetCode, u.getEmail());
		EmailService.sendPswReset(email, resetCode);
		return OfyService.get(OfyService.saveSync(token));

	}

	public static PendingToken checkResetCode(String resetCode) {
		return OfyService.get(tokenClazz, resetCode);
	}

	public static User completeResetPsw(String newPswHash, String resetCode) {
		PendingToken pending = OfyService.get(tokenClazz, resetCode);
		User user = UserResource.getByEmail(pending.getEmail());
		user.setPasswordHash(newPswHash);
		OfyService.delete(pending);
		return OfyService.get(OfyService.saveSync(user));

	}
}
