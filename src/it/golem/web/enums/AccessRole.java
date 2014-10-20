package it.golem.web.enums;


public enum AccessRole {

	ADMIN, EMPLOYEE, USER, SERVICE, UNREGISTERED;

	public static AccessRole prev(AccessRole a) {
		if(a.equals(UNREGISTERED) || a.equals(SERVICE))
			return a;
		AccessRole[] roles = {AccessRole.ADMIN,AccessRole.EMPLOYEE,AccessRole.USER};
		for (int i = 0; i < roles.length; i++) {
			if (a.compareTo(roles[i]) == -1)
				return roles[i];
		}
		return USER;
	}

	public static AccessRole next(AccessRole a) {
		if(a.equals(UNREGISTERED) || a.equals(SERVICE))
			return a;
		AccessRole[] roles = {AccessRole.ADMIN,AccessRole.EMPLOYEE,AccessRole.USER};
		for (int i = roles.length - 1; i > -1; i--) {
			if (a.compareTo(roles[i]) == 1)
				return roles[i];
		}
		return ADMIN;
	}

}
